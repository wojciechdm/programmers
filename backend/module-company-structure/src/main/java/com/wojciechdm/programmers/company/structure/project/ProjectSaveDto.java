package com.wojciechdm.programmers.company.structure.project;

import lombok.*;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public
class ProjectSaveDto {

  private String name;
  private String codeName;
  private LocalDate startDate;
  private LocalDate endDate;
  private ProjectStatus status;
  private Long clientId;
}
