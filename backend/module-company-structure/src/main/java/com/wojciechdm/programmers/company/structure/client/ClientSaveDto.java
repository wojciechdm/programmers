package com.wojciechdm.programmers.company.structure.client;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public
class ClientSaveDto {

  private String name;
  private String codeName;
  private Long keyAccount;
}
