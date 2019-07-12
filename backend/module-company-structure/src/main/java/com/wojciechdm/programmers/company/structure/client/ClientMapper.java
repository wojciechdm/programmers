package com.wojciechdm.programmers.company.structure.client;

import java.time.LocalDate;

class ClientMapper {

 ClientDisplayDto toDisplayDto(Client client) {
    return new ClientDisplayDto(
        client.getId(),
        client.getName(),
        client.getCodeName(),
        client.getKeyAccount(),
        client.getCreateDate(),
        client.getLastModificationDate());
  }

  Client toEntity(long id, ClientSaveDto clientSaveDto){
    return new Client(
        id,
        clientSaveDto.getName(),
        clientSaveDto.getCodeName(),
        clientSaveDto.getKeyAccount(),
        null,
        LocalDate.now());
  }

  Client toEntity(ClientSaveDto clientSaveDto) {
    return new Client(
        null,
        clientSaveDto.getName(),
        clientSaveDto.getCodeName(),
        clientSaveDto.getKeyAccount(),
        LocalDate.now(),
        LocalDate.now());
  }
}
