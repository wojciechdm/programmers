import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ClientService } from '../../../_shared/services/rest-services/client/client.service';
import { Observable, Subscription, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { EmployeeDisplayInterface } from '../../../_shared/models/employee/employee-display.interface';
import { DialogService, MessageService } from 'primeng/api';
import { ClientSaveInterface } from '../../../_shared/models/client/client-save.interface';
import { EmployeeKeyAccountComponent } from '../../employees/employee-key-account/employee-key-account.component';
import { ClientDisplayInterface } from '../../../_shared/models/client/client-display.interface';
import { ActivatedRoute } from '@angular/router';
import { EmployeeService } from '../../../_shared/services/rest-services/employee/employee.service';
import { Message } from '../../../_shared/message.enum';
import { LoginComponent } from '../../login/login.component';
import { LoginService } from 'src/app/_shared/services/rest-services/login/login.service';

@Component({
  selector: 'app-client-update',
  templateUrl: './client-update.component.html',
  styleUrls: ['./client-update.component.scss'],
  providers: [DialogService, MessageService]
})
export class ClientUpdateComponent implements OnInit, OnDestroy {
  private _updateClientForm: FormGroup;
  private subscription: Subscription;
  private codeNamePattern = '[A-Z-]+';
  private keyAccount: EmployeeDisplayInterface = {
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
    private clientService: ClientService,
    private employeeService: EmployeeService,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private dialogService: DialogService,
    private messageService: MessageService,
    private loginService: LoginService
  ) {}

  public ngOnInit(): void {
    this._id = parseInt(this.route.snapshot.queryParamMap.get('id'), 10);
    this.subscription = this.clientService
      .getClient(this._id)
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

  public get updateClientForm(): FormGroup {
    return this._updateClientForm;
  }

  public get id(): number {
    return this._id;
  }

  public onUpdate(): void {
    this.subscription = this.clientService
      .updateClient(this._id, this.createClientToUpdate())
      .pipe(
        retry(1),
        catchError(error => this.handleError(error))
      )
      .subscribe(() => this.showSuccessMessage());
  }

  public onConfirm(): void {
    this.messageService.clear('notUniqueCodename');
  }

  public onDeleteKeyAccount() {
    this.keyAccount = {
      id: null,
      firstName: null,
      lastName: null,
      pesel: null,
      employmentDate: null,
      status: null,
      roles: null
    };
    this._updateClientForm.get('keyAccount').setValue(null);
  }

  public onSelectKeyAccount(): void {
    const ref = this.dialogService.open(EmployeeKeyAccountComponent, {
      header: Message.CLIENT_SELECT_KEY_ACCOUNT,
      contentStyle: {
        'max-width': '100rem',
        'max-height': '60rem',
        overflow: 'auto'
      }
    });

    ref.onClose.subscribe((employee: EmployeeDisplayInterface) => {
      if (employee) {
        this.keyAccount = employee;
        this._updateClientForm
          .get('keyAccount')
          .setValue(employee.firstName + ' ' + employee.lastName);
      }
    });
  }

  public isKeyAccountNotPresent(): boolean {
    return this._updateClientForm.get('keyAccount').value === null;
  }

  public isInvalid(): boolean {
    return this._updateClientForm.invalid;
  }

  private createForm(clientCurrent: ClientDisplayInterface): void {
    if (clientCurrent.keyAccount === null) {
      this._updateClientForm = this.formBuilder.group({
        name: [clientCurrent.name, [Validators.required]],
        codeName: [
          clientCurrent.codeName,
          [Validators.required, Validators.pattern(this.codeNamePattern)]
        ],
        keyAccount: { value: null, disabled: true }
      });
    } else {
      this.subscription = this.employeeService
        .getEmployee(clientCurrent.keyAccount)
        .pipe(
          retry(1),
          catchError(error => this.handleError(error))
        )
        .subscribe(data => {
          this.keyAccount = data;
          this._updateClientForm = this.formBuilder.group({
            name: [clientCurrent.name, [Validators.required]],
            codeName: [
              clientCurrent.codeName,
              [Validators.required, Validators.pattern(this.codeNamePattern)]
            ],
            keyAccount: {
              value: data.firstName + ' ' + data.lastName,
              disabled: true
            }
          });
        });
    }
  }

  private createClientToUpdate(): ClientSaveInterface {
    return {
      name: this._updateClientForm.value.name,
      codeName: this._updateClientForm.value.codeName,
      keyAccount: this.keyAccount.id
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
      detail: Message.CLIENT_CHANGED + this._id,
      closable: false
    });
  }

  private showNotUniqeCodeNameMessage(): void {
    this.messageService.add({
      key: 'notUniqueCodename',
      severity: 'error',
      detail: Message.CLIENT_NOT_UNIQUE_CODE_NAME,
      closable: false
    });
  }

  private handleError(error): Observable<never> {
    if (error.status === 409) {
      this.showNotUniqeCodeNameMessage();
    } else if (error.status === 401) {
      this.loginService.logout();
      this.login();
    } else {
      window.alert(Message.APPLICATION_ERROR);
    }
    return throwError(error.error);
  }
}
