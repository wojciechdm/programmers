import { Component, OnDestroy, OnInit } from '@angular/core';
import { DialogService, MessageService } from 'primeng/api';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Observable, Subscription, throwError } from 'rxjs';
import { ProjectDisplayInterface } from '../../../_shared/models/project/project-display.interface';
import { EmployeeDisplayInterface } from '../../../_shared/models/employee/employee-display.interface';
import { ProjectallocationService } from '../../../_shared/services/rest-services/projectallocation/projectallocation.service';
import { ProjectallocationSaveInterface } from '../../../_shared/models/projectallocation/projectallocation-save.interface';
import { catchError, retry } from 'rxjs/operators';
import { ProjectForAllocationSaveComponent } from '../../projects/project-for-allocation-save/project-for-allocation-save.component';
import { EmployeeKeyAccountComponent } from '../../employees/employee-key-account/employee-key-account.component';
import { ActivatedRoute } from '@angular/router';
import { ProjectService } from '../../../_shared/services/rest-services/project/project.service';
import { EmployeeService } from '../../../_shared/services/rest-services/employee/employee.service';
import { ProjectallocationDisplayInterface } from '../../../_shared/models/projectallocation/projectallocation-display.interface';
import { EmployeeRole } from '../../../_shared/models/employee/employee-role.enum';
import { EmployeeLevel } from '../../../_shared/models/employee/employee-level.enum';
import { Message } from '../../../_shared/message.enum';
import { MessageTranslator } from '../../../_shared/message-translator';
import { LoginComponent } from '../../login/login.component';
import { LoginService } from 'src/app/_shared/services/rest-services/login/login.service';

@Component({
  selector: 'app-projectallocation-update',
  templateUrl: './projectallocation-update.component.html',
  styleUrls: ['./projectallocation-update.component.scss'],
  providers: [DialogService, MessageService]
})
export class ProjectallocationUpdateComponent implements OnInit, OnDestroy {
  private _updateProjectallocationForm: FormGroup;
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
  public _employee: EmployeeDisplayInterface = {
    id: null,
    firstName: null,
    lastName: null,
    pesel: null,
    employmentDate: null,
    status: null,
    roles: null
  };
  private _id: number;

  constructor(
    private projectallocationService: ProjectallocationService,
    private projectService: ProjectService,
    private employeeService: EmployeeService,
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private dialogService: DialogService,
    private messageService: MessageService,
    private loginService: LoginService
  ) {}

  public ngOnInit(): void {
    this._id = parseInt(this.route.snapshot.queryParamMap.get('id'), 10);
    this.subscription = this.projectallocationService
      .getProjectallocation(this._id)
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

  public get updateProjectallocationForm(): FormGroup {
    return this._updateProjectallocationForm;
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

  public get id(): number {
    return this._id;
  }

  public onConfirm(): void {
    this.messageService.clear('invalidProjectallocation');
  }

  public onUpdate(): void {
    this.subscription = this.projectallocationService
      .updateProjectallocation(this._id, this.createProjectallocationToUpdate())
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
        this._updateProjectallocationForm.get('project').setValue(project.name);
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
        this._updateProjectallocationForm
          .get('employee')
          .setValue(employee.firstName + ' ' + employee.lastName);
      }
    });
  }

  public isPercentileWorkloadPresent(): boolean {
    return this._updateProjectallocationForm.value.percentileWorkload > 0;
  }

  public isHoursPerMonthWorkloadPresent(): boolean {
    return this._updateProjectallocationForm.value.hoursPerMonthWorkload > 0;
  }

  public isInvalid(): boolean {
    return (
      this._updateProjectallocationForm.invalid ||
      this._project.id === null ||
      this.employee.id === null ||
      ((this._updateProjectallocationForm.value.percentileWorkload === null ||
        this._updateProjectallocationForm.value.percentileWorkload === 0) &&
        (this._updateProjectallocationForm.value.hoursPerMonthWorkload ===
          null ||
          this._updateProjectallocationForm.value.hoursPerMonthWorkload === 0))
    );
  }

  private createForm(
    projectallocationCurrent: ProjectallocationDisplayInterface
  ): void {
    this.subscription = this.employeeService
      .getEmployee(projectallocationCurrent.employeeId)
      .pipe(
        retry(1),
        catchError(error => this.handleError(error))
      )
      .subscribe(employeeData => {
        this._employee = employeeData;
        this.projectService
          .getProject(projectallocationCurrent.projectId)
          .pipe(
            retry(1),
            catchError(error => this.handleError(error))
          )
          .subscribe(projectData => {
            this._project = projectData;
            this._updateProjectallocationForm = this.formBuilder.group({
              project: [
                { value: projectData.name, disabled: true },
                [Validators.required]
              ],
              employee: [
                {
                  value: employeeData.firstName + ' ' + employeeData.lastName,
                  disabled: true
                },
                [Validators.required]
              ],
              startDate: [
                projectallocationCurrent.startDate,
                [Validators.required]
              ],
              endDate: projectallocationCurrent.endDate,
              percentileWorkload: projectallocationCurrent.percentileWorkload,
              hoursPerMonthWorkload:
                projectallocationCurrent.hoursPerMonthWorkload,
              role: [projectallocationCurrent.role, [Validators.required]],
              level: [projectallocationCurrent.level, [Validators.required]],
              rateMonthly: [
                projectallocationCurrent.rateMonthly,
                [Validators.required]
              ]
            });
          });
      });
  }

  private createProjectallocationToUpdate(): ProjectallocationSaveInterface {
    return {
      projectId: this._project.id,
      employeeId: this.employee.id,
      startDate: this._updateProjectallocationForm.value.startDate,
      endDate: this._updateProjectallocationForm.value.endDate,
      percentileWorkload: this._updateProjectallocationForm.value
        .percentileWorkload,
      hoursPerMonthWorkload: this._updateProjectallocationForm.value
        .hoursPerMonthWorkload,
      role: this._updateProjectallocationForm.value.role,
      level: this._updateProjectallocationForm.value.level,
      rateMonthly: this._updateProjectallocationForm.value.rateMonthly
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

    ref.onClose.subscribe(() => {});
  }

  private showSuccessMessage(): void {
    this.messageService.add({
      key: 'success',
      severity: 'success',
      detail: Message.PROJECT_ALLOCATION_CHANGED + this._id,
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
