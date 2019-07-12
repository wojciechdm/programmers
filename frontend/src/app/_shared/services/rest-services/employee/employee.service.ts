import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../../environments/environment';
import { Observable } from 'rxjs';
import { ApiResponseInterface } from '../../../models/api-response-interface';
import { EmployeeDisplayInterface } from '../../../models/employee/employee-display.interface';
import { EmployeeSaveInterface } from '../../../models/employee/employee-save.interface';
import { ProjectallocationDisplayInterface } from '../../../models/projectallocation/projectallocation-display.interface';
import { EmployeeSortProperty } from 'src/app/_shared/models/employee/employee-sort-property.enum';

enum ENDPOINTS {
  EMPLOYEES = 'employees',
  EMPLOYEE = 'employees/'
}

type EmployeeResponse = ApiResponseInterface<EmployeeDisplayInterface>;

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {
  constructor(private http: HttpClient) {}

  public getAllEmployees(
    page: number,
    limit: number,
    sortProperty: EmployeeSortProperty,
    sortDesc: boolean
  ): Observable<EmployeeResponse> {
    return this.http.get<EmployeeResponse>(
      environment.myapi.url +
        ENDPOINTS.EMPLOYEES +
        `?page=${page}&limit=${limit}&sortProperty=${sortProperty}&sortDesc=${sortDesc}`
    );
  }

  public getEmployee(id: number): Observable<EmployeeDisplayInterface> {
    return this.http.get<EmployeeDisplayInterface>(
      environment.myapi.url + ENDPOINTS.EMPLOYEE + id
    );
  }

  public getEmployeeProjectAllocations(
    id: number
  ): Observable<ProjectallocationDisplayInterface[]> {
    return this.http.get<ProjectallocationDisplayInterface[]>(
      environment.myapi.url + ENDPOINTS.EMPLOYEE + id + '/projectallocations'
    );
  }

  public deleteEmployee(id: number): Observable<unknown> {
    return this.http.delete<unknown>(
      environment.myapi.url + ENDPOINTS.EMPLOYEE + id
    );
  }

  public saveEmployee(
    employee: EmployeeSaveInterface
  ): Observable<EmployeeDisplayInterface> {
    return this.http.post<EmployeeDisplayInterface>(
      environment.myapi.url + ENDPOINTS.EMPLOYEES,
      employee
    );
  }

  public updateEmployee(
    id: number,
    employee: EmployeeSaveInterface
  ): Observable<EmployeeDisplayInterface> {
    return this.http.put<EmployeeDisplayInterface>(
      environment.myapi.url + ENDPOINTS.EMPLOYEE + id,
      employee
    );
  }
}
