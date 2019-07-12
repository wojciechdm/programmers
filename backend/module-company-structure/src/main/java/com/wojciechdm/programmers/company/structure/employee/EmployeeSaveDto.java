package com.wojciechdm.programmers.company.structure.employee;

import lombok.*;
import java.time.LocalDate;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmployeeSaveDto {

  private String firstName;
  private String lastName;
  private String pesel;
  private LocalDate employmentDate;
  private EmployeeStatus status;
  private Map<EmployeeRole, EmployeeLevel> roles;
}
