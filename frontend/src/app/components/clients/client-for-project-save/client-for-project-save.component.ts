import { Component, OnDestroy, OnInit } from '@angular/core';
import { ApiResponseInterface } from '../../../_shared/models/api-response-interface';
import { ClientDisplayInterface } from '../../../_shared/models/client/client-display.interface';
import { Observable, Subscription, throwError } from 'rxjs';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/api';
import { catchError, retry } from 'rxjs/operators';
import { ClientService } from '../../../_shared/services/rest-services/client/client.service';
import { Message } from '../../../_shared/message.enum';
import { ElementsPerPage } from '../../../_shared/elements-per-page';
import { ClientSortProperty } from 'src/app/_shared/models/client/client-sort-property.enum';

type ClientResponse = ApiResponseInterface<ClientDisplayInterface>;

@Component({
  selector: 'app-client-for-project-save',
  templateUrl: './client-for-project-save.component.html',
  styleUrls: ['./client-for-project-save.component.scss']
})
export class ClientForProjectSaveComponent implements OnInit, OnDestroy {
  private _clientResponse: ClientResponse;
  private page = 1;
  private _elementsPerPage: number[] = ElementsPerPage.DATA;
  private _actualElementsPerPage: number = this._elementsPerPage[0];
  private sortProperty: ClientSortProperty = ClientSortProperty.ID;
  private sortDesc: boolean = false;
  private subscription: Subscription;

  constructor(
    private clientService: ClientService,
    private ref: DynamicDialogRef,
    private config: DynamicDialogConfig
  ) {}

  public ngOnInit(): void {
    this.onReload();
  }

  public ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  public get clientResponse(): ClientResponse {
    return this._clientResponse;
  }

  public get elementsPerPage(): number[] {
    return this._elementsPerPage;
  }

  public get actualElementsPerPage(): number {
    return this._actualElementsPerPage;
  }

  public onReload(): void {
    this.subscription = this.clientService
      .getAllClients(this.page, this._actualElementsPerPage, this.sortProperty, this.sortDesc)
      .pipe(
        retry(1),
        catchError(this.handleError)
      )
      .subscribe(readedData => (this._clientResponse = readedData));
  }

  public onSort(sortProperty: ClientSortProperty): void {
    if(sortProperty === this.sortProperty){
      this.sortDesc = !this.sortDesc;
    } else {
      this.sortDesc = false;
      this.sortProperty = sortProperty;
    }    
    this.onReload();
  }

  public isActualSort(sortProperty: ClientSortProperty, sortDesc: boolean): boolean {
    return this.sortProperty === sortProperty && this.sortDesc === sortDesc;
  }

  public onPageChange(event): void {
    this._actualElementsPerPage = event.rows;
    this.page = event.page + 1;
    this.onReload();
  }

  public onSelectClient(client: ClientDisplayInterface): void {
    this.ref.close(client);
  }

  private handleError(error): Observable<never> {
    window.alert(Message.APPLICATION_ERROR);
    return throwError(error.error);
  }
}
