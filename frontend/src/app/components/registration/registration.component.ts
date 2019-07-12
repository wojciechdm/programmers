import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserInterface } from '../../_shared/models/user/user.interface';
import { UserService } from '../../_shared/services/rest-services/user/user.service';
import { Observable, Subscription, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { MessageService } from 'primeng/api';
import { Message } from '../../_shared/message.enum';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss'],
  providers: [MessageService]
})
export class RegistrationComponent implements OnInit, OnDestroy {
  private _form: FormGroup;
  private _user: UserInterface;
  private usernamePasswordPattern = '[a-zA-Z\\d]{5,40}';
  private firstNamePattern = '[a-zA-ZąółęźżśćAÓŁĘŻŹŚĆ]{2,}';
  private lastNamePattern =
    '[a-zA-ZąółęźżśćAÓŁĘŻŹŚĆ]{2,}-?[a-zA-ZąółęźżśćAÓŁĘŻŹŚĆ]+?';
  private cityPattern =
    '[a-zA-ZąółęźżśćAÓŁĘŻŹŚĆ]{2,}[- ]?[a-zA-ZąółęźżśćAÓŁĘŻŹŚĆ]*?[ ]?[a-zA-ZąółęźżśćAÓŁĘŻŹŚĆ]*?';
  private streetPattern =
    '[a-zA-ZąółęźżśćAÓŁĘŻŹŚĆ]{2,}[- ]?[a-zA-ZąółęźżśćAÓŁĘŻŹŚĆ]*?[ ]?[a-zA-ZąółęźżśćAÓŁĘŻŹŚĆ]*?';
  private numberPattern = '\\d+\\w?(/\\d+\\w?)?';
  private zipcodePattern = '\\d{2}-\\d{3}';
  private countryPattern =
    '[a-zA-ZąółęźżśćAÓŁĘŻŹŚĆ()\' -]{2,}';
  private subscription: Subscription;

  constructor(
    private formBuilder: FormBuilder,
    private userService: UserService,
    private messageService: MessageService
  ) {}

  public ngOnInit(): void {
    this.createForm();
  }

  public ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  public get form(): FormGroup {
    return this._form;
  }

  public get user(): UserInterface {
    return this._user;
  }

  public onSubmit(): void {
    this.subscription = this.userService
      .saveUser(this.createUserToSave())
      .pipe(
        retry(1),
        catchError(error => this.handleError(error))
      )
      .subscribe(data => {
        this._user = data;
        this.showSuccessMessage();
        this.createForm();
      });
  }

  public onConfirmSuccess(): void {
    this.messageService.clear('success');
  }

  public onConfirmWarning(): void {
    this.messageService.clear('notUniqueUsername');
  }

  public isInvalid(): boolean {
    return this._form.invalid;
  }

  public getInputCountryClass(): string {
    return (
      (this._form.get('address').get('country').dirty ? 'ng-dirty ' : '') +
      (this._form.get('address').get('country').invalid
        ? 'ng-invalid'
        : 'ng-valid')
    );
  }

  private createForm(): void {
    this._form = this.formBuilder.group({
      username: [
        null,
        [Validators.required, Validators.pattern(this.usernamePasswordPattern)]
      ],
      password: [
        null,
        [Validators.required, Validators.pattern(this.usernamePasswordPattern)]
      ],
      firstName: [
        null,
        [Validators.required, Validators.pattern(this.firstNamePattern)]
      ],
      lastName: [
        null,
        [Validators.required, Validators.pattern(this.lastNamePattern)]
      ],
      age: [null, [Validators.required]],
      dateOfBirth: [null, Validators.required],
      address: this.formBuilder.group({
        city: [
          null,
          [Validators.required, Validators.pattern(this.cityPattern)]
        ],
        street: [
          null,
          [Validators.required, Validators.pattern(this.streetPattern)]
        ],
        number: [
          null,
          [Validators.required, Validators.pattern(this.numberPattern)]
        ],
        zipcode: [
          null,
          [Validators.required, Validators.pattern(this.zipcodePattern)]
        ],
        country: [
          null,
          [Validators.required, Validators.pattern(this.countryPattern)]
        ]
      }),
      title: 'pan'
    });
  }

  private createUserToSave(): UserInterface {
    return {
      userLogin: {
        username: this._form.value.username,
        password: this._form.value.password
      },
      firstName: this._form.value.firstName,
      lastName: this._form.value.lastName,
      age: this._form.value.age,
      dateOfBirth: this._form.value.dateOfBirth,
      address: {
        city: this._form.value.address.city,
        street: this._form.value.address.street,
        number: this._form.value.address.number,
        zipcode: this._form.value.address.zipcode,
        country: this._form.value.address.country
      },
      title: this._form.value.title
    };
  }

  private showSuccessMessage(): void {
    this.messageService.add({
      key: 'success',
      severity: 'success',
      detail: Message.REGISTRATION_SUCCESS,
      closable: false
    });
  }

  private showNotUniqeUsernameMessage(): void {
    this.messageService.add({
      key: 'notUniqueUsername',
      severity: 'error',
      detail: Message.REGISTRATION_NOT_UNIQUE_USERNAME,
      closable: false
    });
  }

  private handleError(error): Observable<never> {
    if (error.status === 409) {
      this.showNotUniqeUsernameMessage();
    } else {
      window.alert(Message.APPLICATION_ERROR);
    }
    return throwError(error.error);
  }
}
