package com.wojciechdm.programmers.company.structure.employee;

import lombok.*;

@Getter
@AllArgsConstructor
public enum EmployeeLevel {
    JUNIOR(1),
    PROFESSIONAL(2),
    SENIOR(3);

    int numericLevel;
}
