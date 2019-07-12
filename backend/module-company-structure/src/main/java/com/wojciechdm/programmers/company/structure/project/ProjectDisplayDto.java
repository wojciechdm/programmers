package com.wojciechdm.programmers.company.structure.project;

import lombok.*;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public
class ProjectDisplayDto {

  private Long id;
  private String name;
  private String codeName;
  private LocalDate startDate;
  private LocalDate endDate;
  private ProjectStatus status;
  private LocalDate createDate;
  private LocalDate lastModificationDate;
  private Long clientId;
}
