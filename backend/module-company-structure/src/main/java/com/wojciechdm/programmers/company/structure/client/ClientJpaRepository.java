package com.wojciechdm.programmers.company.structure.client;

import static java.util.Objects.*;

import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class ClientJpaRepository implements ClientDao {

  private EntityManagerFactory entityManagerFactory;

  @Override
  public Client save(Client client) {
    try {
      EntityManager entityManager = entityManagerFactory.createEntityManager();
      entityManager.getTransaction().begin();

      TypedQuery<Long> typedQuery =
          entityManager.createQuery(
              "SELECT c.id FROM Client c WHERE c.codeName = :codeName", Long.class);
      typedQuery.setParameter("codeName", client.getCodeName());

      if (!typedQuery.getResultList().isEmpty()) {
        throw new ClientCodeNameNotUniqueException("Not unique code name of client during save");
      }

      Client clientSaved = entityManager.merge(client);
      entityManager.getTransaction().commit();
      entityManager.close();
      return clientSaved;
    } catch (PersistenceException e) {
      if ((e.getCause() instanceof ConstraintViolationException)) {
        throw new ClientIllegalFieldValueException("Invalid value of client field during save", e);
      } else {
        throw new IllegalStateException("Problem with database", e);
      }
    }
  }

  @Override
  public Client update(Client client) {
    try {
      EntityManager entityManager = entityManagerFactory.createEntityManager();
      entityManager.getTransaction().begin();

      TypedQuery<Long> typedQuery =
          entityManager.createQuery(
              "SELECT c.id FROM Client As c WHERE c.codeName = :codeName", Long.class);
      typedQuery.setParameter("codeName", client.getCodeName());

      if (!typedQuery.getResultList().isEmpty()
          && !client.getId().equals(typedQuery.getSingleResult())) {
        throw new ClientCodeNameNotUniqueException("Not unique code name of client during update");
      }

      client.setCreateDate(entityManager.find(Client.class, client.getId()).getCreateDate());
      Client clientSaved = entityManager.merge(client);
      entityManager.getTransaction().commit();
      entityManager.close();
      return clientSaved;
    } catch (PersistenceException e) {
      if ((e.getCause() instanceof ConstraintViolationException)) {
        throw new ClientIllegalFieldValueException(
            "Invalid value of client field during update", e);
      } else {
        throw new IllegalStateException("Problem with database", e);
      }
    }
  }

  @Override
  public boolean delete(long id) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    Optional.ofNullable(entityManager.find(Client.class, id)).ifPresent(entityManager::remove);
    Client client = entityManager.find(Client.class, id);
    entityManager.getTransaction().commit();
    entityManager.close();
    return isNull(client);
  }

  @Override
  public Optional<Client> fetch(long id) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    Optional<Client> client = Optional.ofNullable(entityManager.find(Client.class, id));
    entityManager.close();
    return client;
  }

  @Override
  public boolean exists(long id) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    Client client = entityManager.find(Client.class, id);
    entityManager.close();
    return nonNull(client);
  }

  @Override
  public List<Client> fetchAll(
      int page, int limit, ClientSortProperty sortProperty, boolean sortDesc) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    TypedQuery<Client> typedQuery =
        entityManager.createQuery(
            "SELECT c FROM Client AS c ORDER BY "
                + sortProperty.getColumnName()
                + (sortDesc ? " DESC" : ""),
            Client.class);
    typedQuery.setFirstResult((page - 1) * limit);
    typedQuery.setMaxResults(limit);
    List<Client> clients =
        Optional.ofNullable(typedQuery.getResultList()).orElse(Collections.emptyList());
    entityManager.close();
    return clients;
  }

  @Override
  public long count() {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    TypedQuery<Long> typedQuery =
        entityManager.createQuery("SELECT COUNT(*) FROM Client", Long.class);
    long rowsNumber = typedQuery.getSingleResult();
    entityManager.close();
    return rowsNumber;
  }
}
