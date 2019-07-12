package com.wojciechdm.programmers.company.structure.client;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
public class ClientFetchAllResponse {
  private long total;
  private List<ClientDisplayDto> data;
}
