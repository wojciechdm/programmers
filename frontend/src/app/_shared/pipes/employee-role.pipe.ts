import { Pipe, PipeTransform } from '@angular/core';
import { EmployeeRole } from '../models/employee/employee-role.enum';

@Pipe({
  name: 'employeeRole'
})
export class EmployeeRolePipe implements PipeTransform {
  transform(value: string, args?: any): string {
    return EmployeeRole[value];
  }
}
