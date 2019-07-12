package com.wojciechdm.programmers.company.structure.employee;

import static javax.persistence.EnumType.*;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "employee")
class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  private String pesel;

  @Column(name = "employment_date")
  private LocalDate employmentDate;

  private EmployeeStatus status;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "employee_role", joinColumns = @JoinColumn(name = "employee_id"))
  @Enumerated(STRING)
  @MapKeyColumn(name = "role")
  @Column(name = "level")
  private Map<EmployeeRole, EmployeeLevel> roles;
}
