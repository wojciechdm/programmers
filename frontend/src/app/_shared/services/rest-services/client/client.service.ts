import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { environment } from "../../../../../environments/environment";
import { Observable } from "rxjs";
import { ClientDisplayInterface } from "../../../models/client/client-display.interface";
import { ClientSaveInterface } from "../../../models/client/client-save.interface";
import { ApiResponseInterface } from "../../../models/api-response-interface";
import { ProjectDisplayInterface } from "../../../models/project/project-display.interface";
import { ClientSortProperty } from "src/app/_shared/models/client/client-sort-property.enum";

enum ENDPOINTS {
  CLIENTS = "clients",
  CLIENT = "clients/"
}

type ClientResponse = ApiResponseInterface<ClientDisplayInterface>;

@Injectable({
  providedIn: "root"
})
export class ClientService {
  constructor(private http: HttpClient) {}

  public getAllClients(
    page: number,
    limit: number,
    sortProperty: ClientSortProperty,
    sortDesc: boolean
  ): Observable<ClientResponse> {
    return this.http.get<ClientResponse>(
      environment.myapi.url +
        ENDPOINTS.CLIENTS +
        `?page=${page}&limit=${limit}&sortProperty=${sortProperty}&sortDesc=${sortDesc}`
    );
  }

  public getClient(id: number): Observable<ClientDisplayInterface> {
    return this.http.get<ClientDisplayInterface>(
      environment.myapi.url + ENDPOINTS.CLIENT + id
    );
  }

  public getClientProjects(id: number): Observable<ProjectDisplayInterface[]> {
    return this.http.get<ProjectDisplayInterface[]>(
      environment.myapi.url + ENDPOINTS.CLIENT + id + "/projects"
    );
  }

  public deleteClient(id: number): Observable<unknown> {
    return this.http.delete<unknown>(
      environment.myapi.url + ENDPOINTS.CLIENT + id
    );
  }

  public saveClient(
    client: ClientSaveInterface
  ): Observable<ClientDisplayInterface> {
    return this.http.post<ClientDisplayInterface>(
      environment.myapi.url + ENDPOINTS.CLIENTS,
      client
    );
  }

  public updateClient(
    id: number,
    client: ClientSaveInterface
  ): Observable<ClientDisplayInterface> {
    return this.http.put<ClientDisplayInterface>(
      environment.myapi.url + ENDPOINTS.CLIENT + id,
      client
    );
  }
}
