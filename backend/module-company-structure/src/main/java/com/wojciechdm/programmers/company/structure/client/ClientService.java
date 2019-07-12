package com.wojciechdm.programmers.company.structure.client;

import lombok.AllArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
class ClientService {

  private ClientDao clientDao;
  private ClientMapper clientMapper;

  ClientDisplayDto save(ClientSaveDto clientSaveDto) {
    return clientMapper.toDisplayDto(clientDao.save(clientMapper.toEntity(clientSaveDto)));
  }

  ClientDisplayDto update(long id, ClientSaveDto clientSaveDto) {
    return Optional.of(clientMapper.toEntity(id, clientSaveDto))
        .filter(client -> clientDao.exists(client.getId()))
        .map(clientDao::update)
        .map(clientMapper::toDisplayDto)
        .orElseThrow(() -> new ClientNotFoundException("Client id: " + id + " not found"));
  }

  boolean delete(long id) {
    return clientDao.delete(id);
  }

  ClientDisplayDto fetch(long id) {
    return clientDao
        .fetch(id)
        .map(clientMapper::toDisplayDto)
        .orElseThrow(() -> new ClientNotFoundException("Client id: " + id + " not found"));
  }

  ClientFetchAllResponse fetchAll(
      int page, int limit, ClientSortProperty sortProperty, boolean sortDesc) {
    return new ClientFetchAllResponse(
        clientDao.count(),
        clientDao.fetchAll(page, limit, sortProperty, sortDesc).stream()
            .map(clientMapper::toDisplayDto)
            .collect(Collectors.toList()));
  }
}
