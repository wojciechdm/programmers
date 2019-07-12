package com.wojciechdm.programmers.company.structure.projectallocation;

import static javax.persistence.EnumType.*;

import com.wojciechdm.programmers.company.structure.employee.EmployeeLevel;
import com.wojciechdm.programmers.company.structure.employee.EmployeeRole;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "project_allocation")
class ProjectAllocation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "project_id")
  private Long projectId;

  @Column(name = "employee_id")
  private Long employeeId;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  @Column(name = "percentile_workload")
  private Integer percentileWorkload;

  @Column(name = "hours_per_month_workload")
  private Integer hoursPerMonthWorkload;

  @Enumerated(STRING)
  private EmployeeRole role;

  @Enumerated(STRING)
  private EmployeeLevel level;

  @Column(name = "rate_monthly")
  private Integer rateMonthly;
}
