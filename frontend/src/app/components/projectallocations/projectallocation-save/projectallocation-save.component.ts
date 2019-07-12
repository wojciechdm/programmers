import {Component, OnDestroy, OnInit} from '@angular/core';
import {DialogService, MessageService} from 'primeng/api';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Observable, Subscription, throwError} from 'rxjs';
import {catchError, retry} from 'rxjs/operators';
import {ProjectallocationSaveInterface} from '../../../_shared/models/projectallocation/projectallocation-save.interface';
import {ProjectallocationService} from '../../../_shared/services/rest-services/projectallocation/projectallocation.service';
import {ProjectDisplayInterface} from '../../../_shared/models/project/project-display.interface';
import {EmployeeDisplayInterface} from '../../../_shared/models/employee/employee-display.interface';
import {EmployeeKeyAccountComponent} from '../../employees/employee-key-account/employee-key-account.component';
import {ProjectForAllocationSaveComponent} from '../../projects/project-for-allocation-save/project-for-allocation-save.component';
import {EmployeeRole} from '../../../_shared/models/employee/employee-role.enum';
import {EmployeeLevel} from '../../../_shared/models/employee/employee-level.enum';
import {Message} from '../../../_shared/message.enum';
import {MessageTranslator} from '../../../_shared/message-translator';
import {LoginComponent} from '../../login/login.component';
import {LoginService} from 'src/app/_shared/services/rest-services/login/login.service';

@Component({
  selector: 'app-projectallocation-save',
  templateUrl: './projectallocation-save.component.html',
  styleUrls: ['./projectallocation-save.component.scss'],
  providers: [DialogService, MessageService]
})
export class ProjectallocationSaveComponent implements OnInit, OnDestroy {
  private _saveProjectallocationForm: FormGroup;
  private subscription: Subscription;
  private _roles: string[] = Object.keys(EmployeeRole);
  private _levels: string[] = Object.keys(EmployeeLevel);
  private _project: ProjectDisplayInterface = {
    id: null,
    name: null,
    codeName: null,
    startDate: null,
    endDate: null,
    status: null,
    createDate: null,
    lastModificationDate: null,
    clientId: null
  };
  private _employee: EmployeeDisplayInterface = {
    id: null,
    firstName: null,
    lastName: null,
    pesel: null,
    employmentDate: null,
    status: null,
    roles: null
  };

  constructor(
    private projectallocationService: ProjectallocationService,
    private formBuilder: FormBuilder,
    private dialogService: DialogService,
    private messageService: MessageService,
    private loginService: LoginService
  ) {
  }

  public ngOnInit(): void {
    this.createForm();
  }

  public ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  public get saveProjectallocationForm(): FormGroup {
    return this._saveProjectallocationForm;
  }

  public get roles(): string[] {
    return this._roles;
  }

  public get levels(): string[] {
    return this._levels;
  }

  public get employee(): EmployeeDisplayInterface {
    return this._employee;
  }

  public get project(): ProjectDisplayInterface {
    return this._project;
  }

  public onConfirm(): void {
    this.messageService.clear('invalidProjectallocation');
  }

  public onSave(): void {
    this.subscription = this.projectallocationService
      .saveProjectallocation(this.createProjectallocationToSave())
      .pipe(
        retry(1),
        catchError(error => this.handleError(error))
      )
      .subscribe(() => this.showSuccessMessage());
  }

  public onSelectProject() {
    const ref = this.dialogService.open(ProjectForAllocationSaveComponent, {
      header: Message.PROJECT_ALLOCATION_SELECT_PROJECT,
      contentStyle: {
        'max-width': '100rem',
        'max-height': '60rem',
        overflow: 'auto'
      }
    });

    ref.onClose.subscribe((project: ProjectDisplayInterface) => {
      if (project) {
        this._project = project;
        this._saveProjectallocationForm.get('project').setValue(project.name);
      }
    });
  }

  public onSelectEmployee() {
    const ref = this.dialogService.open(EmployeeKeyAccountComponent, {
      header: Message.PROJECT_ALLOCATION_SELECT_EMPLOYEE,
      contentStyle: {
        'max-width': '100rem',
        'max-height': '60rem',
        overflow: 'auto'
      }
    });

    ref.onClose.subscribe((employee: EmployeeDisplayInterface) => {
      if (employee) {
        this._employee = employee;
        this._saveProjectallocationForm
          .get('employee')
          .setValue(employee.firstName + ' ' + employee.lastName);
      }
    });
  }

  public isPercentileWorkloadPresent(): boolean {
    return this._saveProjectallocationForm.value.percentileWorkload > 0;
  }

  public isHoursPerMonthWorkloadPresent(): boolean {
    return this._saveProjectallocationForm.value.hoursPerMonthWorkload > 0;
  }

  public isInvalid(): boolean {
    return (
      this._saveProjectallocationForm.invalid ||
      this._project.id === null ||
      this.employee.id === null ||
      ((this._saveProjectallocationForm.value.percentileWorkload === null ||
        this._saveProjectallocationForm.value.percentileWorkload === 0) &&
        (this._saveProjectallocationForm.value.hoursPerMonthWorkload === null ||
          this._saveProjectallocationForm.value.hoursPerMonthWorkload === 0))
    );
  }

  private createForm(): void {
    this._saveProjectallocationForm = this.formBuilder.group({
      project: [{value: null, disabled: true}, [Validators.required]],
      employee: [{value: null, disabled: true}, [Validators.required]],
      startDate: [null, [Validators.required]],
      endDate: null,
      percentileWorkload: null,
      hoursPerMonthWorkload: null,
      role: [null, [Validators.required]],
      level: [null, [Validators.required]],
      rateMonthly: [null, [Validators.required]]
    });
  }

  private createProjectallocationToSave(): ProjectallocationSaveInterface {
    return {
      projectId: this._project.id,
      employeeId: this.employee.id,
      startDate: this._saveProjectallocationForm.value.startDate,
      endDate: this._saveProjectallocationForm.value.endDate,
      percentileWorkload: this._saveProjectallocationForm.value
        .percentileWorkload,
      hoursPerMonthWorkload: this._saveProjectallocationForm.value
        .hoursPerMonthWorkload,
      role: this._saveProjectallocationForm.value.role,
      level: this._saveProjectallocationForm.value.level,
      rateMonthly: this._saveProjectallocationForm.value.rateMonthly
    };
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

  private showSuccessMessage(): void {
    this.messageService.add({
      key: 'success',
      severity: 'success',
      detail: Message.PROJECT_ALLOCATION_ADDED,
      closable: false
    });
  }

  private showInvalidProjectallocationMessage(message: string): void {
    this.messageService.add({
      key: 'invalidProjectallocation',
      severity: 'error',
      detail: message,
      closable: false,
      life: 60000
    });
  }

  private handleError(error): Observable<never> {
    if (error.status === 409) {
      this.showInvalidProjectallocationMessage(
        MessageTranslator.translateProjectAllocationRulesViolations(error.error)
      );
    } else if (error.status === 401) {
      this.loginService.logout();
      this.login();
    } else {
      window.alert(Message.APPLICATION_ERROR);
    }
    return throwError(error.error);
  }
}
