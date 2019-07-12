import { Pipe, PipeTransform } from '@angular/core';
import { EmployeeLevel } from '../models/employee/employee-level.enum';

@Pipe({
  name: 'employeeLevel'
})
export class EmployeeLevelPipe implements PipeTransform {
  transform(value: string, args?: any): string {
    return EmployeeLevel[value];
  }
}
