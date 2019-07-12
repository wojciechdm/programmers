import { EmployeeStatus } from "./employee-status.enum";
import { EmployeeRole } from "./employee-role.enum";
import { EmployeeLevel } from "./employee-level.enum";

export interface EmployeeDisplayInterface {
  id: number;
  firstName: string;
  lastName: string;
  pesel: string;
  employmentDate: string;
  status: EmployeeStatus;
  roles: Map<EmployeeRole, EmployeeLevel>;
}
