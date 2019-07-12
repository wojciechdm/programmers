package com.wojciechdm.programmers.company.structure.client;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.*;

public class ClientServiceFacade {

  private ClientService clientService;

  public ClientServiceFacade(ClientDao clientDao) {
    clientService = new ClientService(clientDao, new ClientMapper());
  }

  public ClientDisplayDto save(ClientSaveDto clientSaveDto) {

    checkArgument(nonNull(clientSaveDto), "Client can not be null");

    return clientService.save(clientSaveDto);
  }

  public ClientDisplayDto update(long id, ClientSaveDto clientSaveDto) {

    checkArgument(nonNull(clientSaveDto), "Client can not be null");

    return clientService.update(id, clientSaveDto);
  }

  public boolean delete(long id) {

    return clientService.delete(id);
  }

  public ClientDisplayDto fetch(long id) {

    return clientService.fetch(id);
  }

  public ClientFetchAllResponse fetchAll(
      int page, int limit, ClientSortProperty sortProperty, boolean sortDesc) {

    return clientService.fetchAll(page, limit, sortProperty, sortDesc);
  }
}
