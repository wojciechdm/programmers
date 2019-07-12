import { Component, OnDestroy, OnInit } from '@angular/core';
import { ClientService } from '../../../_shared/services/rest-services/client/client.service';
import { ActivatedRoute } from '@angular/router';
import { ClientDisplayInterface } from '../../../_shared/models/client/client-display.interface';
import { Observable, Subscription, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { ProjectDisplayInterface } from '../../../_shared/models/project/project-display.interface';
import { Message } from '../../../_shared/message.enum';

@Component({
  selector: 'app-client',
  templateUrl: './client-show.component.html',
  styleUrls: ['./client-show.component.scss']
})
export class ClientShowComponent implements OnInit, OnDestroy {
  private _client: ClientDisplayInterface;
  private _projects: ProjectDisplayInterface[] = [];
  private subscription: Subscription;

  constructor(
    private clientService: ClientService,
    private route: ActivatedRoute
  ) {}

  public ngOnInit(): void {
    const id = parseInt(this.route.snapshot.queryParamMap.get('id'), 10);
    this.subscription = this.clientService
      .getClient(id)
      .pipe(
        retry(1),
        catchError(this.handleError)
      )
      .subscribe(data => {
        this._client = data;
        this.clientService
          .getClientProjects(id)
          .pipe(
            retry(1),
            catchError(this.handleError)
          )
          .subscribe(dataProjects => (this._projects = dataProjects));
      });
  }

  public ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  public get client(): ClientDisplayInterface {
    return this._client;
  }

  public get projects(): ProjectDisplayInterface[] {
    return this._projects;
  }

  public isProjectsPresent(): boolean {
    return this._projects.length > 0;
  }

  private handleError(error): Observable<never> {
    window.alert(Message.APPLICATION_ERROR);
    return throwError(error.error);
  }
}
