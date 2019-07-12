import { Injectable } from '@angular/core';
import { ApiResponseInterface } from '../../../models/api-response-interface';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../../environments/environment';
import { ProjectallocationDisplayInterface } from '../../../models/projectallocation/projectallocation-display.interface';
import { ProjectallocationSaveInterface } from '../../../models/projectallocation/projectallocation-save.interface';
import { ProjectallocationSortProperty } from 'src/app/_shared/models/projectallocation/projectallocation-sort-property.enum';

enum ENDPOINTS {
  PROJECTALLOCATIONS = 'projectallocations',
  PROJECTALLOCATION = 'projectallocations/'
}

type ProjectallocationResponse = ApiResponseInterface<
  ProjectallocationDisplayInterface
>;

@Injectable({
  providedIn: 'root'
})
export class ProjectallocationService {
  constructor(private http: HttpClient) {}

  public getAllProjectallocations(
    page: number,
    limit: number,
    sortProperty: ProjectallocationSortProperty,
    sortDesc: boolean
  ): Observable<ProjectallocationResponse> {
    return this.http.get<ProjectallocationResponse>(
      environment.myapi.url +
        ENDPOINTS.PROJECTALLOCATIONS +
        `?page=${page}&limit=${limit}&sortProperty=${sortProperty}&sortDesc=${sortDesc}`
    );
  }

  public getProjectallocation(
    id: number
  ): Observable<ProjectallocationDisplayInterface> {
    return this.http.get<ProjectallocationDisplayInterface>(
      environment.myapi.url + ENDPOINTS.PROJECTALLOCATION + id
    );
  }

  public deleteProjectallocation(id: number): Observable<unknown> {
    return this.http.delete<unknown>(
      environment.myapi.url + ENDPOINTS.PROJECTALLOCATION + id
    );
  }

  public saveProjectallocation(
    projectallocation: ProjectallocationSaveInterface
  ): Observable<ProjectallocationDisplayInterface> {
    return this.http.post<ProjectallocationDisplayInterface>(
      environment.myapi.url + ENDPOINTS.PROJECTALLOCATIONS,
      projectallocation
    );
  }

  public updateProjectallocation(
    id: number,
    projectallocation: ProjectallocationSaveInterface
  ): Observable<ProjectallocationDisplayInterface> {
    return this.http.put<ProjectallocationDisplayInterface>(
      environment.myapi.url + ENDPOINTS.PROJECTALLOCATION + id,
      projectallocation
    );
  }
}
