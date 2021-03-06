package com.wojciechdm.programmers.company.structure.projectallocation;

import com.wojciechdm.programmers.company.structure.employee.EmployeeLevel;
import com.wojciechdm.programmers.company.structure.employee.EmployeeRole;
import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectAllocationSaveDto {

  private Long projectId;
  private Long employeeId;
  private LocalDate startDate;
  private LocalDate endDate;
  private Integer percentileWorkload;
  private Integer hoursPerMonthWorkload;
  private EmployeeRole role;
  private EmployeeLevel level;
  private Integer rateMonthly;
}
