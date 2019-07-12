package com.wojciechdm.programmers.company.structure.client;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "client")
class Client {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Column(name = "code_name")
  private String codeName;

  @Column(name = "key_account")
  private Long keyAccount;

  @Column(name = "create_date")
  private LocalDate createDate;

  @Column(name = "last_modification_date")
  private LocalDate lastModificationDate;
}
