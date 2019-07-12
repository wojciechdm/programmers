import { EmployeeRole } from "../employee/employee-role.enum";
import { EmployeeLevel } from "../employee/employee-level.enum";

export interface ProjectallocationDisplayInterface {
  id: number;
  projectId: number;
  employeeId: number;
  startDate: string;
  endDate: string;
  percentileWorkload: number;
  hoursPerMonthWorkload: number;
  role: EmployeeRole;
  level: EmployeeLevel;
  rateMonthly: number;
}
