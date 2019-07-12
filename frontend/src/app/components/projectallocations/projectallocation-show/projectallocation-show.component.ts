import { Component, OnDestroy, OnInit } from '@angular/core';
import { Observable, Subscription, throwError } from 'rxjs';
import { ProjectallocationDisplayInterface } from '../../../_shared/models/projectallocation/projectallocation-display.interface';
import { ActivatedRoute } from '@angular/router';
import { catchError, retry } from 'rxjs/operators';
import { ProjectallocationService } from '../../../_shared/services/rest-services/projectallocation/projectallocation.service';
import { Message } from '../../../_shared/message.enum';

@Component({
  selector: 'app-projectallocation-show',
  templateUrl: './projectallocation-show.component.html',
  styleUrls: ['./projectallocation-show.component.scss']
})
export class ProjectallocationShowComponent implements OnInit, OnDestroy {
  private _projectallocation: ProjectallocationDisplayInterface;
  private subscription: Subscription;

  constructor(
    private projectallocationService: ProjectallocationService,
    private route: ActivatedRoute
  ) {}

  public ngOnInit(): void {
    const id = parseInt(this.route.snapshot.queryParamMap.get('id'), 10);
    this.subscription = this.projectallocationService
      .getProjectallocation(id)
      .pipe(
        retry(1),
        catchError(this.handleError)
      )
      .subscribe(data => {
        this._projectallocation = data;
      });
  }

  public ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  public get projectallocation(): ProjectallocationDisplayInterface {
    return this._projectallocation;
  }

  private handleError(error): Observable<never> {
    window.alert(Message.APPLICATION_ERROR);
    return throwError(error.error);
  }
}
