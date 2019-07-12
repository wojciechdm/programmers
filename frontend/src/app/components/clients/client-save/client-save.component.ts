import { Component, OnDestroy, OnInit } from '@angular/core';
import { ClientService } from '../../../_shared/services/rest-services/client/client.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { catchError, retry } from 'rxjs/operators';
import { Observable, Subscription, throwError } from 'rxjs';
import { DialogService, MessageService } from 'primeng/api';
import { EmployeeKeyAccountComponent } from '../../employees/employee-key-account/employee-key-account.component';
import { EmployeeDisplayInterface } from '../../../_shared/models/employee/employee-display.interface';
import { ClientSaveInterface } from '../../../_shared/models/client/client-save.interface';
import { Message } from '../../../_shared/message.enum';
import { LoginComponent } from '../../login/login.component';
import { LoginService } from 'src/app/_shared/services/rest-services/login/login.service';

@Component({
  selector: 'app-client-save',
  templateUrl: './client-save.component.html',
  styleUrls: ['./client-save.component.scss'],
  providers: [DialogService, MessageService]
})
export class ClientSaveComponent implements OnInit, OnDestroy {
  private _saveClientForm: FormGroup;
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

  constructor(
    private clientService: ClientService,
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

  public get saveClientForm(): FormGroup {
    return this._saveClientForm;
  }

  public onConfirm(): void {
    this.messageService.clear('notUniqueCodename');
  }

  public onSave(): void {
    this.subscription = this.clientService
      .saveClient(this.createClientToSave())
      .pipe(
        retry(1),
        catchError(error => this.handleError(error))
      )
      .subscribe(() => this.showSuccessMessage());
  }

  public onDeleteKeyAccount(): void {
    this.keyAccount = {
      id: null,
      firstName: null,
      lastName: null,
      pesel: null,
      employmentDate: null,
      status: null,
      roles: null
    };
    this._saveClientForm.get('keyAccount').setValue(null);
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
        this._saveClientForm
          .get('keyAccount')
          .setValue(employee.firstName + ' ' + employee.lastName);
      }
    });
  }

  public isInvalid(): boolean {
    return this._saveClientForm.invalid;
  }

  public isKeyAccountNotPresent(): boolean {
    return this.keyAccount.id === null;
  }

  private createForm(): void {
    this._saveClientForm = this.formBuilder.group({
      name: [null, [Validators.required]],
      codeName: [
        null,
        [Validators.required, Validators.pattern(this.codeNamePattern)]
      ],
      keyAccount: { value: null, disabled: true }
    });
  }

  private createClientToSave(): ClientSaveInterface {
    return {
      name: this._saveClientForm.value.name,
      codeName: this._saveClientForm.value.codeName,
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
      detail: Message.CLIENT_ADDED,
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
