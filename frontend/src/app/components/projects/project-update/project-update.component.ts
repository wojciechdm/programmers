import { Component, OnDestroy, OnInit } from '@angular/core';
import { DialogService, MessageService } from 'primeng/api';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Observable, Subscription, throwError } from 'rxjs';
import { ClientDisplayInterface } from '../../../_shared/models/client/client-display.interface';
import { ClientService } from '../../../_shared/services/rest-services/client/client.service';
import { ActivatedRoute } from '@angular/router';
import { ProjectService } from '../../../_shared/services/rest-services/project/project.service';
import { catchError, retry } from 'rxjs/operators';
import { ProjectSaveInterface } from '../../../_shared/models/project/project-save.interface';
import { ClientForProjectSaveComponent } from '../../clients/client-for-project-save/client-for-project-save.component';
import { ProjectDisplayInterface } from '../../../_shared/models/project/project-display.interface';
import { ProjectStatus } from '../../../_shared/models/project/project-status.enum';
import { Message } from '../../../_shared/message.enum';
import { LoginComponent } from '../../login/login.component';
import { LoginService } from 'src/app/_shared/services/rest-services/login/login.service';

@Component({
  selector: 'app-project-update',
  templateUrl: './project-update.component.html',
  styleUrls: ['./project-update.component.scss'],
  providers: [DialogService, MessageService]
})
export class ProjectUpdateComponent implements OnInit, OnDestroy {
  private _updateProjectForm: FormGroup;
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
  private _id: number;

  constructor(
    private projectService: ProjectService,
    private clientService: ClientService,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private dialogService: DialogService,
    private messageService: MessageService,
    private loginService: LoginService
  ) {}

  public ngOnInit(): void {
    this._id = parseInt(this.route.snapshot.queryParamMap.get('id'), 10);
    this.subscription = this.projectService
      .getProject(this._id)
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

  public get updateProjectForm(): FormGroup {
    return this._updateProjectForm;
  }

  public get client(): ClientDisplayInterface {
    return this._client;
  }

  public get statuses(): string[] {
    return this._statuses;
  }

  public get id(): number {
    return this._id;
  }

  public onConfirm(): void {
    this.messageService.clear('notUniqueCodename');
  }

  public onUpdate(): void {
    this.subscription = this.projectService
      .updateProject(this._id, this.createProjectToUpdate())
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
        this._updateProjectForm.get('client').setValue(client.name);
      }
    });
  }

  public isInvalid(): boolean {
    return this._updateProjectForm.invalid || this._client.id === null;
  }

  private createForm(projectCurrent: ProjectDisplayInterface): void {
    this.subscription = this.clientService
      .getClient(projectCurrent.clientId)
      .pipe(
        retry(1),
        catchError(error => this.handleError(error))
      )
      .subscribe(data => {
        this._client = data;
        this._updateProjectForm = this.formBuilder.group({
          name: [projectCurrent.name, [Validators.required]],
          codeName: [
            projectCurrent.codeName,
            [Validators.required, Validators.pattern(this.codeNamePattern)]
          ],
          startDate: [projectCurrent.startDate, [Validators.required]],
          endDate: projectCurrent.endDate,
          status: [projectCurrent.status, [Validators.required]],
          client: [
            { value: this._client.name, disabled: true },
            [Validators.required]
          ]
        });
      });
  }

  private createProjectToUpdate(): ProjectSaveInterface {
    return {
      name: this._updateProjectForm.value.name,
      codeName: this._updateProjectForm.value.codeName,
      startDate: this._updateProjectForm.value.startDate,
      endDate: this._updateProjectForm.value.endDate,
      status: this._updateProjectForm.value.status,
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
      detail: Message.PROJECT_CHANGED + this._id,
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
