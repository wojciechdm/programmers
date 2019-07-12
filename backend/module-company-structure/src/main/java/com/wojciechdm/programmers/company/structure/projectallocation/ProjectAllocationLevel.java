package com.wojciechdm.programmers.company.structure.projectallocation;

import lombok.*;

@Getter
@AllArgsConstructor
enum ProjectAllocationLevel {
  JUNIOR(1),
  PROFESSIONAL(2),
  SENIOR(3);

  int numericLevel;
}
