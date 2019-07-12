import { Pipe, PipeTransform } from '@angular/core';
import { EmployeeRole } from '../models/employee/employee-role.enum';
import { EmployeeLevel } from '../models/employee/employee-level.enum';

@Pipe({
  name: 'employeeRoleLevel'
})
export class EmployeeRoleLevelPipe implements PipeTransform {

  transform(roles: Map<EmployeeRole, EmployeeLevel>, args?: any): string {
    let result: string = '';
    Object.keys(roles).forEach((key) => {result = result + EmployeeLevel[roles[key]] + ' ' + EmployeeRole[key] + '\r\n';});
    return result;
  }

}
