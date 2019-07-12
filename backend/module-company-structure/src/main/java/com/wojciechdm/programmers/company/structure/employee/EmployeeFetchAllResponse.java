package com.wojciechdm.programmers.company.structure.employee;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
public class EmployeeFetchAllResponse {
  private long total;
  private List<EmployeeDisplayDto> data;
}
