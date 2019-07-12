import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Observable, Subscription, throwError } from 'rxjs';
import { EmployeeService } from '../../../_shared/services/rest-services/employee/employee.service';
import { catchError, retry } from 'rxjs/operators';
import { EmployeeSaveInterface } from '../../../_shared/models/employee/employee-save.interface';
import { MessageService, DialogService } from 'primeng/api';
import { EmployeeStatus } from '../../../_shared/models/employee/employee-status.enum';
import { EmployeeRole } from '../../../_shared/models/employee/employee-role.enum';
import { EmployeeLevel } from '../../../_shared/models/employee/employee-level.enum';
import { Message } from '../../../_shared/message.enum';
import { LoginComponent } from '../../login/login.component';
import { LoginService } from 'src/app/_shared/services/rest-services/login/login.service';

@Component({
  selector: 'app-employee-save',
  templateUrl: './employee-save.component.html',
  styleUrls: ['./employee-save.component.scss'],
  providers: [DialogService, MessageService]
})
export class EmployeeSaveComponent implements OnInit, OnDestroy {
  private _saveEmployeeForm: FormGroup;
  private subscription: Subscription;
  private namePattern = '[a-zA-ZąółęźżśćAÓŁĘŻŹŚĆ]{2,}';
  private peselPattern = '\\d{11}';
  private _statuses: string[] = Object.keys(EmployeeStatus);
  private _roles: string[] = Object.keys(EmployeeRole);
  private _levels: string[] = Object.keys(EmployeeLevel);

  constructor(
    private employeeService: EmployeeService,
    private formBuilder: FormBuilder,
    private dialogService: DialogService,
    private messageService: MessageService,
    private loginService: LoginService
  ) {}

  public ngOnInit(): void {
    this.createForm();
  }

  public ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  public get saveEmployeeForm(): FormGroup {
    return this._saveEmployeeForm;
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

  public onSave(): void {
    this.subscription = this.employeeService
      .saveEmployee(this.createEmployeeToSave())
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

  public isHiddenDeleteButton(roleNumber: number): boolean {
    return roleNumber === 0;
  }

  public isInvalid(): boolean {
    return this._saveEmployeeForm.invalid;
  }

  private createForm(): void {
    this._saveEmployeeForm = this.formBuilder.group({
      firstName: [
        null,
        [Validators.required, Validators.pattern(this.namePattern)]
      ],
      lastName: [
        null,
        [Validators.required, Validators.pattern(this.namePattern)]
      ],
      pesel: [null, Validators.pattern(this.peselPattern)],
      employmentDate: null,
      status: [null, Validators.required],
      roles: this.formBuilder.array([
        this.formBuilder.group({
          role: [null, Validators.required],
          level: [null, Validators.required]
        })
      ])
    });
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
      detail: Message.EMPLOYEE_ADDED,
      closable: false
    });
  }

  private getRolesFormArray(): FormArray {
    return this._saveEmployeeForm.controls.roles as FormArray;
  }

  private createEmployeeToSave(): EmployeeSaveInterface {
    return {
      firstName: this._saveEmployeeForm.value.firstName,
      lastName: this._saveEmployeeForm.value.lastName,
      pesel: this._saveEmployeeForm.value.pesel,
      employmentDate: this._saveEmployeeForm.value.employmentDate,
      roles: this._saveEmployeeForm.value.roles
        .map(role => ({ [role.role]: role.level }))
        .reduce((accumulator, element) => {
          return Object.assign(accumulator, element);
        }, {}),
      status: this._saveEmployeeForm.value.status
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
