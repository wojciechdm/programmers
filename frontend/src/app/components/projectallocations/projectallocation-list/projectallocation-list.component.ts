import {Component, OnDestroy, OnInit} from '@angular/core';
import {ApiResponseInterface} from '../../../_shared/models/api-response-interface';
import {ProjectallocationDisplayInterface} from '../../../_shared/models/projectallocation/projectallocation-display.interface';
import {forkJoin, Observable, Subscription, throwError} from 'rxjs';
import {catchError, retry} from 'rxjs/operators';
import {ProjectallocationService} from '../../../_shared/services/rest-services/projectallocation/projectallocation.service';
import {Message} from '../../../_shared/message.enum';
import {ElementsPerPage} from '../../../_shared/elements-per-page';
import {DialogService} from 'primeng/api';
import {LoginComponent} from '../../login/login.component';
import {LoginService} from 'src/app/_shared/services/rest-services/login/login.service';
import {ProjectallocationSortProperty} from 'src/app/_shared/models/projectallocation/projectallocation-sort-property.enum';
import {ClientSortProperty} from '../../../_shared/models/client/client-sort-property.enum';

type ProjectallocationResponse = ApiResponseInterface<ProjectallocationDisplayInterface>;

@Component({
  selector: 'app-projectallocation-list',
  templateUrl: './projectallocation-list.component.html',
  styleUrls: ['./projectallocation-list.component.scss'],
  providers: [DialogService]
})
export class ProjectallocationListComponent implements OnInit, OnDestroy {
  private _projectallocationsResponse: ProjectallocationResponse;
  private _checkedAll: boolean;
  private page = 1;
  private _checked: number[] = [];
  private _elementsPerPage: number[] = ElementsPerPage.DATA;
  private _actualElementsPerPage: number = this._elementsPerPage[0];
  private sortProperty: ProjectallocationSortProperty = ProjectallocationSortProperty.ID;
  private sortDesc: boolean = false;
  private subscription: Subscription;

  constructor(
    private projectallocationService: ProjectallocationService,
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

  public get projectallocationsResponse(): ProjectallocationResponse {
    return this._projectallocationsResponse;
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
    this.subscription = this.projectallocationService
      .getAllProjectallocations(this.page, this._actualElementsPerPage, this.sortProperty, this.sortDesc)
      .pipe(
        retry(1),
        catchError(error => this.handleError(error))
      )
      .subscribe(readedData => (this._projectallocationsResponse = readedData));
  }

  public onSort(sortProperty: ProjectallocationSortProperty): void {
    if(sortProperty === this.sortProperty){
      this.sortDesc = !this.sortDesc;
    } else {
      this.sortDesc = false;
      this.sortProperty = sortProperty;
    }    
    this.onReload();
  }

  public isActualSort(sortProperty: ProjectallocationSortProperty, sortDesc: boolean): boolean {
    return this.sortProperty === sortProperty && this.sortDesc === sortDesc;
  }

  public onDelete(): void {

    const observables = this._checked.map((idToDelete) => {
      return this.projectallocationService.deleteProjectallocation(idToDelete);
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
      for (const projectallocation of this._projectallocationsResponse.data) {
        this._checked.push(projectallocation.id);
      }
    }
    this._checkedAll = !this._checkedAll;
    this.onReload();
  }

  public onCheck(): void {
    this._checkedAll =
      this._checked.length === this._projectallocationsResponse.data.length;
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
