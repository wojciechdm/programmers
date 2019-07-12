package com.wojciechdm.programmers.company.structure.projectallocation;

import lombok.*;

@AllArgsConstructor
@Getter
public enum ProjectAllocationSortProperty {
  ID("id"),
  PROJECT("project_id"),
  EMPLOYEE("employee_id"),
  START_DATE("start_date"),
  END_DATE("end_date"),
  RATE_MONTHLY("rate_monthly");

  private String columnName;
}
