import { EmployeeLevel } from "./employee-level.enum";

export interface EmployeeSaveInterface {
  firstName: string;
  lastName: string;
  pesel: string;
  employmentDate: string;
  status: EmployeeLevel;
  roles: { EmployeeRole: EmployeeLevel };
}
