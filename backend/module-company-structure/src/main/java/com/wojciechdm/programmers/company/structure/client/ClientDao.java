package com.wojciechdm.programmers.company.structure.client;

import java.util.*;

interface ClientDao {

  Client save(Client client);

  Client update(Client client);

  boolean delete(long id);

  Optional<Client> fetch(long id);

  boolean exists(long id);

  List<Client> fetchAll(int page, int limit, ClientSortProperty sortProperty, boolean sortDesc);

  long count();
}
