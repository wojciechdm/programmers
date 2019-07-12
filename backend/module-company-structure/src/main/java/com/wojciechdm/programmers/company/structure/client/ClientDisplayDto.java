package com.wojciechdm.programmers.company.structure.client;

import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public
class ClientDisplayDto {

  private Long id;
  private String name;
  private String codeName;
  private Long keyAccount;
  private LocalDate createDate;
  private LocalDate lastModificationDate;
}
