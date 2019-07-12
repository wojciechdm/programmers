import { Component, OnDestroy, OnInit } from '@angular/core';
import { ApiResponseInterface } from '../../../_shared/models/api-response-interface';
import { Observable, Subscription, throwError } from 'rxjs';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/api';
import { catchError, retry } from 'rxjs/operators';
import { ProjectDisplayInterface } from '../../../_shared/models/project/project-display.interface';
import { ProjectService } from '../../../_shared/services/rest-services/project/project.service';
import { Message } from '../../../_shared/message.enum';
import { ElementsPerPage } from '../../../_shared/elements-per-page';
import { ProjectSortProperty } from 'src/app/_shared/models/project/project-sort-property.enum';

type ProjectResponse = ApiResponseInterface<ProjectDisplayInterface>;

@Component({
  selector: 'app-project-for-allocation-save',
  templateUrl: './project-for-allocation-save.component.html',
  styleUrls: ['./project-for-allocation-save.component.scss']
})
export class ProjectForAllocationSaveComponent implements OnInit, OnDestroy {
  private _projectResponse: ProjectResponse;
  private page = 1;
  private _elementsPerPage: number[] = ElementsPerPage.DATA;
  private _actualElementsPerPage: number = this._elementsPerPage[0];
  private sortProperty: ProjectSortProperty = ProjectSortProperty.ID;
  private sortDesc: boolean = false;
  private subscription: Subscription;

  constructor(
    private projectService: ProjectService,
    private ref: DynamicDialogRef,
    private config: DynamicDialogConfig
  ) {}

  public ngOnInit(): void {
    this.onReload();
  }

  public ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  public get projectResponse(): ProjectResponse {
    return this._projectResponse;
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
        catchError(this.handleError)
      )
      .subscribe(readedData => (this._projectResponse = readedData));
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

  public onPageChange(event): void {
    this._actualElementsPerPage = event.rows;
    this.page = event.page + 1;
    this.onReload();
  }

  public onSelectProject(project: ProjectDisplayInterface): void {
    this.ref.close(project);
  }

  private handleError(error): Observable<never> {
    window.alert(Message.APPLICATION_ERROR);
    return throwError(error.error);
  }
}
