package com.wojciechdm.programmers.company.structure.project;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
public class ProjectFetchAllResponse {
  private long total;
  private List<ProjectDisplayDto> data;
}
