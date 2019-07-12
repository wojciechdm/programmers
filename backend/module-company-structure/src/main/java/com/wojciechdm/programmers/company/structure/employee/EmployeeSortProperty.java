package com.wojciechdm.programmers.company.structure.employee;

import lombok.*;

@AllArgsConstructor
@Getter
public enum EmployeeSortProperty {
  ID("id"),
  FIRST_NAME("first_name"),
  LAST_NAME("last_name"),
  PESEL("pesel"),
  EMPLOYMENT_DATE("employment_date");

  private String columnName;
}
