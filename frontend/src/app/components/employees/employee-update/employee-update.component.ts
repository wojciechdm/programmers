import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EmployeeService } from '../../../_shared/services/rest-services/employee/employee.service';
import { catchError, retry } from 'rxjs/operators';
import { Observable, Subscription, throwError } from 'rxjs';
import { EmployeeSaveInterface } from '../../../_shared/models/employee/employee-save.interface';
import { ActivatedRoute } from '@angular/router';
import { EmployeeDisplayInterface } from '../../../_shared/models/employee/employee-display.interface';
import { MessageService, DialogService } from 'primeng/api';
import { EmployeeStatus } from '../../../_shared/models/employee/employee-status.enum';
import { EmployeeRole } from '../../../_shared/models/employee/employee-role.enum';
import { EmployeeLevel } from '../../../_shared/models/employee/employee-level.enum';
import { Message } from '../../../_shared/message.enum';
import { LoginComponent } from '../../login/login.component';
import { LoginService } from 'src/app/_shared/services/rest-services/login/login.service';

@Component({
  selector: 'app-employee-update',
  templateUrl: './employee-update.component.html',
  styleUrls: ['./employee-update.component.scss'],
  providers: [DialogService, MessageService]
})
export class EmployeeUpdateComponent implements OnInit, OnDestroy {
  private _updateEmployeeForm: FormGroup;
  private subscription: Subscription;
  private namePattern = '[a-zA-ZąółęźżśćAÓŁĘŻŹŚĆ]{2,}';
  private peselPattern = '\\d{11}';
  private _statuses: string[] = Object.keys(EmployeeStatus);
  private _roles: string[] = Object.keys(EmployeeRole);
  private _levels: string[] = Object.keys(EmployeeLevel);
  private _id: number;

  constructor(
    private employeeService: EmployeeService,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private dialogService: DialogService,
    private messageService: MessageService,
    private loginService: LoginService
  ) {}

  public ngOnInit(): void {
    this._id = parseInt(this.route.snapshot.queryParamMap.get('id'), 10);
    this.subscription = this.employeeService
      .getEmployee(this._id)
      .pipe(
        retry(1),
        catchError(error => this.handleError(error))
      )
      .subscribe(data => this.createForm(data));
  }

  public ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  public get updateEmployeeForm(): FormGroup {
    return this._updateEmployeeForm;
  }

  public get statuses(): string[] {
    return this._statuses;
  }

  public get roles(): string[] {
    return this._roles;
  }

  public get levels(): string[] {
    return this._levels;
  }

  public get id(): number {
    return this._id;
  }

  public onUpdate(): void {
    this.subscription = this.employeeService
      .updateEmployee(this._id, this.createEmployeeToUpdate())
      .pipe(
        retry(1),
        catchError(error => this.handleError(error))
      )
      .subscribe(() => this.showSuccessMessage());
  }

  public onAddRole(): void {
    this.getRolesFormArray().push(
      this.formBuilder.group({
        role: [null, Validators.required],
        level: [null, Validators.required]
      })
    );
  }

  public onDeleteRole(roleNumber: number): void {
    this.getRolesFormArray().removeAt(roleNumber);
  }

  public isInvalid(): boolean {
    return this._updateEmployeeForm.invalid;
  }

  public isHiddenDeleteButton(roleNumber: number): boolean {
    return roleNumber === 0;
  }

  private createForm(employeeCurrent: EmployeeDisplayInterface): void {
    this._updateEmployeeForm = this.formBuilder.group({
      firstName: [
        employeeCurrent.firstName,
        [Validators.required, Validators.pattern(this.namePattern)]
      ],
      lastName: [
        employeeCurrent.lastName,
        [Validators.required, Validators.pattern(this.namePattern)]
      ],
      pesel: [employeeCurrent.pesel, Validators.pattern(this.peselPattern)],
      employmentDate: employeeCurrent.employmentDate,
      status: [employeeCurrent.status, Validators.required],
      roles: this.formBuilder.array([])
    });
    if (Object.entries(employeeCurrent.roles).length > 0) {
      for (const [key, value] of Object.entries(employeeCurrent.roles)) {
        this.getRolesFormArray().push(
          this.formBuilder.group({
            role: [key, Validators.required],
            level: [value, Validators.required]
          })
        );
      }
    } else {
      this.onAddRole();
    }
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

    ref.onClose.subscribe(() => {});
  }

  private showSuccessMessage(): void {
    this.messageService.add({
      key: 'success',
      severity: 'success',
      detail: Message.EMPLOYEE_CHANGED + this._id,
      closable: false
    });
  }

  private getRolesFormArray(): FormArray {
    return this._updateEmployeeForm.controls.roles as FormArray;
  }

  private createEmployeeToUpdate(): EmployeeSaveInterface {
    return {
      firstName: this._updateEmployeeForm.value.firstName,
      lastName: this._updateEmployeeForm.value.lastName,
      pesel: this._updateEmployeeForm.value.pesel,
      employmentDate: this._updateEmployeeForm.value.employmentDate,
      roles: this._updateEmployeeForm.value.roles
        .map(role => ({ [role.role]: role.level }))
        .reduce((accumulator, element) => {
          return Object.assign(accumulator, element);
        }, {}),
      status: this._updateEmployeeForm.value.status
    };
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
