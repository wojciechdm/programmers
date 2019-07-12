import {Component, OnDestroy, OnInit} from '@angular/core';
import {ProjectService} from '../../../_shared/services/rest-services/project/project.service';
import {catchError, retry} from 'rxjs/operators';
import {forkJoin, Observable, Subscription, throwError} from 'rxjs';
import {ApiResponseInterface} from '../../../_shared/models/api-response-interface';
import {ProjectDisplayInterface} from '../../../_shared/models/project/project-display.interface';
import {Message} from '../../../_shared/message.enum';
import {ElementsPerPage} from '../../../_shared/elements-per-page';
import {DialogService} from 'primeng/api';
import {LoginComponent} from '../../login/login.component';
import {LoginService} from 'src/app/_shared/services/rest-services/login/login.service';
import {ProjectSortProperty} from 'src/app/_shared/models/project/project-sort-property.enum';

type ProjectResponse = ApiResponseInterface<ProjectDisplayInterface>;

@Component({
  selector: 'app-project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.scss'],
  providers: [DialogService]
})
export class ProjectListComponent implements OnInit, OnDestroy {
  private _projectsResponse: ProjectResponse;
  private _checkedAll: boolean;
  private page = 1;
  private _checked: number[] = [];
  private _elementsPerPage: number[] = ElementsPerPage.DATA;
  private _actualElementsPerPage: number = this._elementsPerPage[0];
  private sortProperty: ProjectSortProperty = ProjectSortProperty.ID;
  private sortDesc: boolean = false;
  private subscription: Subscription;

  constructor(
    private projectService: ProjectService,
    private dialogService: DialogService,
    private loginService: LoginService
  ) {
  }

  public ngOnInit(): void {
    this.onReload();
  }

  public ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  public get projectsResponse(): ProjectResponse {
    return this._projectsResponse;
  }

  public get checkedAll(): boolean {
    return this._checkedAll;
  }

  public get checked(): number[] {
    return this._checked;
  }

  public set checked(value: number[]) {
    this._checked = value;
  }

  public get elementsPerPage(): number[] {
    return this._elementsPerPage;
  }

  public get actualElementsPerPage(): number {
    return this._actualElementsPerPage;
  }

  public onReload(): void {
    this.subscription = this.projectService
      .getAllProjects(this.page, this._actualElementsPerPage, this.sortProperty, this.sortDesc)
      .pipe(
        retry(1),
        catchError(error => this.handleError(error))
      )
      .subscribe(readedData => (this._projectsResponse = readedData));
  }

  public onSort(sortProperty: ProjectSortProperty): void {
    if(sortProperty === this.sortProperty){
      this.sortDesc = !this.sortDesc;
    } else {
      this.sortDesc = false;
      this.sortProperty = sortProperty;
    }    
    this.onReload();
  }

  public isActualSort(sortProperty: ProjectSortProperty, sortDesc: boolean): boolean {
    return this.sortProperty === sortProperty && this.sortDesc === sortDesc;
  }

  public onDelete(): void {

    const observables = this._checked.map((idToDelete) => {
      return this.projectService.deleteProject(idToDelete);
    });

    this.subscription = forkJoin(observables).pipe(
      retry(1),
      catchError(error => this.handleError(error))
    ).subscribe(() => this.onReload());

    this._checked = [];
    this._checkedAll = false;
  }

  public onPageChange(event): void {
    this._actualElementsPerPage = event.rows;
    this.page = event.page + 1;
    this.onReload();
  }

  public onCheckAll(): void {
    if (this._checkedAll) {
      this._checked = [];
    } else {
      for (const project of this._projectsResponse.data) {
        this._checked.push(project.id);
      }
    }
    this._checkedAll = !this._checkedAll;
    this.onReload();
  }

  public onCheck(): void {
    this._checkedAll =
      this._checked.length === this._projectsResponse.data.length;
  }

  public isDeleteDisabled(): boolean {
    return this._checked.length === 0;
  }

  private login(): void {
    const ref = this.dialogService.open(LoginComponent, {
      header: Message.LOGIN,
      contentStyle: {
        'max-width': '45rem',
        'max-height': '65rem',
        overflow: 'auto'
      }
    });

    ref.onClose.subscribe(() => {
    });
  }

  private handleError(error): Observable<never> {
    if (error.status === 401) {
      this.loginService.logout();
      this.login();
    } else {
      window.alert(Message.APPLICATION_ERROR);
    }
    return throwError(error.error);
  }
}
