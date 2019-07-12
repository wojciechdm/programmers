import { Pipe, PipeTransform } from '@angular/core';
import { EmployeeStatus } from '../models/employee/employee-status.enum';

@Pipe({
  name: 'employeeStatus'
})
export class EmployeeStatusPipe implements PipeTransform {
  transform(value: string, args?: any): string {
    return EmployeeStatus[value];
  }
}
