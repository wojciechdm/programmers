package com.wojciechdm.programmers.company.structure.employee;

import lombok.*;

@Getter
@AllArgsConstructor
enum EmployeeSqlStatementParameter {
  INSERT_FIRST_NAME(1),
  INSERT_LAST_NAME(2),
  INSERT_PESEL(3),
  INSERT_EMPLOYMENT_DATE(4),
  INSERT_STATUS(5),
  INSERT_ROLES_ROLE(1),
  INSERT_ROLES_LEVEL(2),
  INSERT_ROLES_EMPLOYEE_ID(3),
  SELECT_ID(1),
  SELECT_ROLES_EMPLOYEE_ID(1),
  UPDATE_FIRST_NAME(1),
  UPDATE_LAST_NAME(2),
  UPDATE_PESEL(3),
  UPDATE_EMPLOYMENT_DATE(4),
  UPDATE_STATUS(5),
  UPDATE_ID(6),
  DELETE_ID(1),
  DELETE_ROLES_EMPLOYEE_ID(1),
  LIMIT_PAGE(1),
  LIMIT_ELEMENTS(2);

  int parameterNumber;
}
