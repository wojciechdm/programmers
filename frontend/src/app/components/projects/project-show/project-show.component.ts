import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProjectService } from '../../../_shared/services/rest-services/project/project.service';
import { Observable, Subscription, throwError } from 'rxjs';
import { ProjectDisplayInterface } from '../../../_shared/models/project/project-display.interface';
import { catchError, retry } from 'rxjs/operators';
import { ProjectallocationDisplayInterface } from '../../../_shared/models/projectallocation/projectallocation-display.interface';
import { Message } from '../../../_shared/message.enum';

@Component({
  selector: 'app-project-show',
  templateUrl: './project-show.component.html',
  styleUrls: ['./project-show.component.scss']
})
export class ProjectShowComponent implements OnInit, OnDestroy {
  private _project: ProjectDisplayInterface;
  private subscription: Subscription;
  private _projectallocations: ProjectallocationDisplayInterface[] = [];

  constructor(
    private projectService: ProjectService,
    private route: ActivatedRoute
  ) {}

  public ngOnInit(): void {
    const id = parseInt(this.route.snapshot.queryParamMap.get('id'), 10);
    this.subscription = this.projectService
      .getProject(id)
      .pipe(
        retry(1),
        catchError(this.handleError)
      )
      .subscribe(data => {
        this._project = data;
        this.projectService
          .getProjectAllocations(id)
          .pipe(
            retry(1),
            catchError(this.handleError)
          )
          .subscribe(
            dataAllocations => (this._projectallocations = dataAllocations)
          );
      });
  }

  public ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  public get project(): ProjectDisplayInterface {
    return this._project;
  }

  public get projectallocations(): ProjectallocationDisplayInterface[] {
    return this._projectallocations;
  }

  public isProjectallocationsPresent(): boolean {
    return this._projectallocations.length > 0;
  }

  private handleError(error): Observable<never> {
    window.alert(Message.APPLICATION_ERROR);
    return throwError(error.error);
  }
}
