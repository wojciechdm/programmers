import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {
  MessageService,
  DynamicDialogRef,
  DynamicDialogConfig,
  DialogService
} from 'primeng/api';
import { Message } from 'src/app/_shared/message.enum';
import { LoginService } from 'src/app/_shared/services/rest-services/login/login.service';
import { Subscription, Observable, throwError } from 'rxjs';
import { retry, catchError } from 'rxjs/operators';
import { UserLoginInterface } from 'src/app/_shared/models/user/user-login.interface';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  providers: [DialogService, MessageService]
})
export class LoginComponent implements OnInit, OnDestroy {
  private subscription: Subscription;
  private _loginForm: FormGroup;
  private pattern = '[a-zA-Z\\d]{5,40}';

  constructor(
    private formBuilder: FormBuilder,
    private messageService: MessageService,
    private ref: DynamicDialogRef,
    private config: DynamicDialogConfig,
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

  public get loginForm(): FormGroup {
    return this._loginForm;
  }

  public onLogin(): void {
    this.subscription = this.loginService
      .login(this.createUserLogin())
      .pipe(
        retry(1),
        catchError(error => this.handleError(error))
      )
      .subscribe(() => this.showSuccessMessage());
  }

  public onClose(): void {
    this.ref.close();
  }

  public onConfirmIncorrectUsernamePassword(): void {
    this.messageService.clear('incorrectUsernamePassword');
  }

  public isInvalid(): boolean {
    return this._loginForm.invalid;
  }

  private createForm(): void {
    this._loginForm = this.formBuilder.group({
      username: [null, [Validators.required, Validators.pattern(this.pattern)]],
      password: [null, [Validators.required, Validators.pattern(this.pattern)]]
    });
  }

  private createUserLogin(): UserLoginInterface {
    return {
      username: this._loginForm.value.username,
      password: this._loginForm.value.password
    };
  }

  private showSuccessMessage(): void {
    this.messageService.add({
      key: 'success',
      severity: 'success',
      detail: Message.LOGIN_SUCCESS,
      closable: false
    });
  }

  private showIncorrectUsernamePasswordMessage(): void {
    this.messageService.add({
      key: 'incorrectUsernamePassword',
      severity: 'error',
      detail: Message.LOGIN_INCORRECT_USERNAME_PASSWORD,
      closable: false
    });
  }

  private handleError(error): Observable<never> {
   if (error.status === 401) {
      this.showIncorrectUsernamePasswordMessage();
    } else {
      window.alert(Message.APPLICATION_ERROR);
    }
   return throwError(error.error);
  }
}
