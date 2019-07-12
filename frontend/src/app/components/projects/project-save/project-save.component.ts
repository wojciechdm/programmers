import { Component, OnDestroy, OnInit } from '@angular/core';
import { DialogService, MessageService } from 'primeng/api';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Observable, Subscription, throwError } from 'rxjs';
import { ClientDisplayInterface } from '../../../_shared/models/client/client-display.interface';
import { ProjectSaveInterface } from '../../../_shared/models/project/project-save.interface';
import { catchError, retry } from 'rxjs/operators';
import { ProjectService } from '../../../_shared/services/rest-services/project/project.service';
import { ClientForProjectSaveComponent } from '../../clients/client-for-project-save/client-for-project-save.component';
import { ProjectStatus } from '../../../_shared/models/project/project-status.enum';
import { Message } from '../../../_shared/message.enum';
import { LoginComponent } from '../../login/login.component';
import { LoginService } from 'src/app/_shared/services/rest-services/login/login.service';

@Component({
  selector: 'app-project-save',
  templateUrl: './project-save.component.html',
  styleUrls: ['./project-save.component.scss'],
  providers: [DialogService, MessageService]
})
export class ProjectSaveComponent implements OnInit, OnDestroy {
  private _saveProjectForm: FormGroup;
  private subscription: Subscription;
  private codeNamePattern = '[A-Z-]+';
  private _client: ClientDisplayInterface = {
    id: null,
    name: null,
    codeName: null,
    keyAccount: null,
    createDate: null,
    lastModificationDate: null
  };
  private _statuses: string[] = Object.keys(ProjectStatus);

  constructor(
    private projectService: ProjectService,
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

  public get saveProjectForm(): FormGroup {
    return this._saveProjectForm;
  }

  public get client(): ClientDisplayInterface {
    return this._client;
  }

  public get statuses(): string[] {
    return this._statuses;
  }

  public onConfirm(): void {
    this.messageService.clear('notUniqueCodename');
  }

  public onSave(): void {
    this.subscription = this.projectService
      .saveProject(this.createProjectToSave())
      .pipe(
        retry(1),
        catchError(error => this.handleError(error))
      )
      .subscribe(() => this.showSuccessMessage());
  }

  public onSelectClient() {
    const ref = this.dialogService.open(ClientForProjectSaveComponent, {
      header: Message.PROJECT_SELECT_CLIENT,
      contentStyle: {
        'max-width': '80rem',
        'max-height': '60rem',
        overflow: 'auto'
      }
    });

    ref.onClose.subscribe((client: ClientDisplayInterface) => {
      if (client) {
        this._client = client;
        this._saveProjectForm.get('client').setValue(client.name);
      }
    });
  }

  public isInvalid(): boolean {
    return this._saveProjectForm.invalid || this._client.id === null;
  }

  private createForm(): void {
    this._saveProjectForm = this.formBuilder.group({
      name: [null, [Validators.required]],
      codeName: [
        null,
        [Validators.required, Validators.pattern(this.codeNamePattern)]
      ],
      startDate: [null, [Validators.required]],
      endDate: null,
      status: [null, [Validators.required]],
      client: [{ value: null, disabled: true }, [Validators.required]]
    });
  }

  private createProjectToSave(): ProjectSaveInterface {
    return {
      name: this._saveProjectForm.value.name,
      codeName: this._saveProjectForm.value.codeName,
      startDate: this._saveProjectForm.value.startDate,
      endDate: this._saveProjectForm.value.endDate,
      status: this._saveProjectForm.value.status,
      clientId: this._client.id
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
      detail: Message.PROJECT_ADDED,
      closable: false
    });
  }

  private showNotUniqeCodeNameMessage(): void {
    this.messageService.add({
      key: 'notUniqueCodename',
      severity: 'error',
      detail: Message.PROJECT_NOT_UNIQUE_CODE_NAME,
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
