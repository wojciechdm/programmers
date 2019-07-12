package com.wojciechdm.programmers.company.structure.projectallocation;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
public class ProjectAllocationFetchAllResponse {
  private long total;
  private List<ProjectAllocationDisplayDto> data;
}
