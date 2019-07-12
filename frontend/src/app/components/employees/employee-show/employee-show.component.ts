import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { catchError, retry } from 'rxjs/operators';
import { Observable, Subscription, throwError } from 'rxjs';
import { EmployeeDisplayInterface } from '../../../_shared/models/employee/employee-display.interface';
import { EmployeeService } from '../../../_shared/services/rest-services/employee/employee.service';
import { ProjectallocationDisplayInterface } from '../../../_shared/models/projectallocation/projectallocation-display.interface';
import { Message } from '../../../_shared/message.enum';

@Component({
  selector: 'app-employee-show',
  templateUrl: './employee-show.component.html',
  styleUrls: ['./employee-show.component.scss']
})
export class EmployeeShowComponent implements OnInit, OnDestroy {
  private _employee: EmployeeDisplayInterface;
  private subscription: Subscription;
  private _projectallocations: ProjectallocationDisplayInterface[] = [];

  constructor(
    private employeeService: EmployeeService,
    private route: ActivatedRoute
  ) {}

  public ngOnInit(): void {
    const id = parseInt(this.route.snapshot.queryParamMap.get('id'), 10);
    this.subscription = this.employeeService
      .getEmployee(id)
      .pipe(
        retry(1),
        catchError(this.handleError)
      )
      .subscribe(data => {
        this._employee = data;
        this.employeeService
          .getEmployeeProjectAllocations(id)
          .pipe(
            retry(1),
            catchError(this.handleError)
          )
          .subscribe(
            dataAllocations => (this._projectallocations = dataAllocations)
          );
      });
  }

  public ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  public get employee(): EmployeeDisplayInterface {
    return this._employee;
  }

  public get projectallocations(): ProjectallocationDisplayInterface[] {
    return this._projectallocations;
  }

  public isProjectallocationsPresent(): boolean {
    return this._projectallocations.length > 0;
  }

  private handleError(error): Observable<never> {
    window.alert(Message.APPLICATION_ERROR);
    return throwError(error.error);
  }
}
