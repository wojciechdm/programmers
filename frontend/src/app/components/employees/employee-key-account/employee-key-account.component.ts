import { Component, OnDestroy, OnInit } from '@angular/core';
import { EmployeeDisplayInterface } from '../../../_shared/models/employee/employee-display.interface';
import { EmployeeService } from '../../../_shared/services/rest-services/employee/employee.service';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/api';
import { ApiResponseInterface } from '../../../_shared/models/api-response-interface';
import { Observable, Subscription, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { Message } from '../../../_shared/message.enum';
import { ElementsPerPage } from '../../../_shared/elements-per-page';
import { EmployeeSortProperty } from 'src/app/_shared/models/employee/employee-sort-property.enum';

type EmployeeResponse = ApiResponseInterface<EmployeeDisplayInterface>;

@Component({
  selector: 'app-employee-key-account',
  templateUrl: './employee-key-account.component.html',
  styleUrls: ['./employee-key-account.component.scss']
})
export class EmployeeKeyAccountComponent implements OnInit, OnDestroy {
  private _employeeResponse: EmployeeResponse;
  private page = 1;
  private _elementsPerPage: number[] = ElementsPerPage.DATA;
  private _actualElementsPerPage: number = this._elementsPerPage[0];
  private sortProperty: EmployeeSortProperty = EmployeeSortProperty.ID;
  private sortDesc: boolean = false;
  private subscription: Subscription;

  constructor(
    private employeeService: EmployeeService,
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

  public get employeeResponse(): EmployeeResponse {
    return this._employeeResponse;
  }

  public get elementsPerPage(): number[] {
    return this._elementsPerPage;
  }

  public get actualElementsPerPage(): number {
    return this._actualElementsPerPage;
  }

  public onReload(): void {
    this.subscription = this.employeeService
      .getAllEmployees(this.page, this.actualElementsPerPage, this.sortProperty, this.sortDesc)
      .pipe(
        retry(1),
        catchError(this.handleError)
      )
      .subscribe(readedData => (this._employeeResponse = readedData));
  }

  public onSort(sortProperty: EmployeeSortProperty): void {
    if(sortProperty === this.sortProperty){
      this.sortDesc = !this.sortDesc;
    } else {
      this.sortDesc = false;
      this.sortProperty = sortProperty;
    }    
    this.onReload();
  }

  public isActualSort(sortProperty: EmployeeSortProperty, sortDesc: boolean): boolean {
    return this.sortProperty === sortProperty && this.sortDesc === sortDesc;
  }

  public onPageChange(event): void {
    this._actualElementsPerPage = event.rows;
    this.page = event.page + 1;
    this.onReload();
  }

  public onSelectEmployee(employee: EmployeeDisplayInterface): void {
    this.ref.close(employee);
  }

  private handleError(error): Observable<never> {
    window.alert(Message.APPLICATION_ERROR);
    return throwError(error.error);
  }
}
