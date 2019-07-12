package com.wojciechdm.programmers.company.structure.project;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.EnumType.STRING;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "project")
class Project {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Column(name = "code_name")
  private String codeName;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  @Enumerated(STRING)
  private ProjectStatus status;

  @Column(name = "create_date")
  private LocalDate createDate;

  @Column(name = "last_modification_date")
  private LocalDate lastModificationDate;

  @Column(name = "client_id")
  private Long clientId;
}
