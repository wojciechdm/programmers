import { Injectable } from '@angular/core';
import { ApiResponseInterface } from '../../../models/api-response-interface';
import { ProjectDisplayInterface } from '../../../models/project/project-display.interface';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../../environments/environment';
import { ProjectSaveInterface } from '../../../models/project/project-save.interface';
import { ProjectallocationDisplayInterface } from '../../../models/projectallocation/projectallocation-display.interface';
import { ProjectSortProperty } from 'src/app/_shared/models/project/project-sort-property.enum';

enum ENDPOINTS {
  PROJECTS = 'projects',
  PROJECT = 'projects/'
}

type ProjectResponse = ApiResponseInterface<ProjectDisplayInterface>;

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  constructor(private http: HttpClient) {}

  public getAllProjects(
    page: number,
    limit: number,
    sortProperty: ProjectSortProperty,
    sortDesc: boolean
  ): Observable<ProjectResponse> {
    return this.http.get<ProjectResponse>(
      environment.myapi.url +
        ENDPOINTS.PROJECTS +
        `?page=${page}&limit=${limit}&sortProperty=${sortProperty}&sortDesc=${sortDesc}`
    );
  }

  public getProject(id: number): Observable<ProjectDisplayInterface> {
    return this.http.get<ProjectDisplayInterface>(
      environment.myapi.url + ENDPOINTS.PROJECT + id
    );
  }

  public getProjectAllocations(
    id: number
  ): Observable<ProjectallocationDisplayInterface[]> {
    return this.http.get<ProjectallocationDisplayInterface[]>(
      environment.myapi.url + ENDPOINTS.PROJECT + id + '/projectallocations'
    );
  }

  public deleteProject(id: number): Observable<unknown> {
    return this.http.delete<unknown>(
      environment.myapi.url + ENDPOINTS.PROJECT + id
    );
  }

  public saveProject(
    project: ProjectSaveInterface
  ): Observable<ProjectDisplayInterface> {
    return this.http.post<ProjectDisplayInterface>(
      environment.myapi.url + ENDPOINTS.PROJECTS,
      project
    );
  }

  public updateProject(
    id: number,
    project: ProjectSaveInterface
  ): Observable<ProjectDisplayInterface> {
    return this.http.put<ProjectDisplayInterface>(
      environment.myapi.url + ENDPOINTS.PROJECT + id,
      project
    );
  }
}
