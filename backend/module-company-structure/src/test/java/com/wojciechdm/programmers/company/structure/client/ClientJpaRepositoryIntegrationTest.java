package com.wojciechdm.programmers.company.structure.client;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.MySQLContainer;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

class ClientJpaRepositoryIntegrationTest {

  private static ClientJpaRepository clientJpaRepository;
  private static MySQLContainer container = new MySQLContainer();
  private static EntityManagerFactory entityManagerFactory;

  @BeforeAll
  static void init() {
    container.withInitScript("database_model.sql");
    container.start();
    Map<String, String> properties = new HashMap<>();
    properties.put(
        "javax.persistence.jdbc.url",
        container.getJdbcUrl() + "?serverTimezone=Europe/Warsaw&useSSL=false");
    properties.put("javax.persistence.jdbc.user", container.getUsername());
    properties.put("javax.persistence.jdbc.password", container.getPassword());
    entityManagerFactory = Persistence.createEntityManagerFactory("persistence", properties);
    clientJpaRepository = new ClientJpaRepository(entityManagerFactory);
  }

  @AfterAll
  static void cleanUp() {
    entityManagerFactory.close();
    container.close();
  }

  @Test
  void shouldCrudClient() {
    // save
    // given
    Client expectedSaved = new Client(null, "qqq", "zzz", null, LocalDate.now(), LocalDate.now());
    String nameExpectedSaved = expectedSaved.getName();
    String codeNameExpectedSaved = expectedSaved.getCodeName();
    Long keyAccountExpectedSaved = expectedSaved.getKeyAccount();
    LocalDate createDateExpectedSaved = expectedSaved.getCreateDate();
    LocalDate lastModificationDateExpectedSaved = expectedSaved.getLastModificationDate();
    // when
    Client actualSaved = clientJpaRepository.save(expectedSaved);
    String nameActualSaved = actualSaved.getName();
    String codeNameActualSaved = actualSaved.getCodeName();
    Long keyAccountActualSaved = actualSaved.getKeyAccount();
    LocalDate createDateActualSaved = actualSaved.getCreateDate();
    LocalDate lastModificationDateActualSaved = actualSaved.getLastModificationDate();
    // then
    assertEquals(nameExpectedSaved, nameActualSaved);
    assertEquals(codeNameExpectedSaved, codeNameActualSaved);
    assertEquals(keyAccountExpectedSaved, keyAccountActualSaved);
    assertEquals(createDateExpectedSaved, createDateActualSaved);
    assertEquals(lastModificationDateExpectedSaved, lastModificationDateActualSaved);

    // exists
    // given
    boolean expectedExists = true;
    // when
    boolean actualExists = clientJpaRepository.exists(3L);
    // then
    assertEquals(expectedExists, actualExists);

    // count
    // given
    long expectedCount = 3L;
    // when
    long actualCount = clientJpaRepository.count();
    // then
    assertEquals(expectedCount, actualCount);

    // fetch
    // when
    Client actualFetch = clientJpaRepository.fetch(3L).get();
    String nameActualFetch = actualFetch.getName();
    String codeNameActualFetch = actualFetch.getCodeName();
    Long keyAccountActualFetch = actualFetch.getKeyAccount();
    LocalDate createDateActualFetch = actualFetch.getCreateDate();
    LocalDate lastModificationDateActualFetch = actualFetch.getLastModificationDate();
    // then
    assertEquals(nameExpectedSaved, nameActualFetch);
    assertEquals(codeNameExpectedSaved, codeNameActualFetch);
    assertEquals(keyAccountExpectedSaved, keyAccountActualFetch);
    assertEquals(createDateExpectedSaved, createDateActualFetch);
    assertEquals(lastModificationDateExpectedSaved, lastModificationDateActualFetch);

    // fetch all
    // when
    List<Client> actualFetchAll = clientJpaRepository.fetchAll(3, 1, ClientSortProperty.ID, false);
    String nameActualFetchAll = actualFetchAll.get(0).getName();
    String codeNameActualFetchAll = actualFetchAll.get(0).getCodeName();
    Long keyAccountActualFetchAll = actualFetchAll.get(0).getKeyAccount();
    LocalDate createDateActualFetchAll = actualFetchAll.get(0).getCreateDate();
    LocalDate lastModificationDateActualFetchAll = actualFetchAll.get(0).getLastModificationDate();
    // then
    assertEquals(nameExpectedSaved, nameActualFetchAll);
    assertEquals(codeNameExpectedSaved, codeNameActualFetchAll);
    assertEquals(keyAccountExpectedSaved, keyAccountActualFetchAll);
    assertEquals(createDateExpectedSaved, createDateActualFetchAll);
    assertEquals(lastModificationDateExpectedSaved, lastModificationDateActualFetchAll);

    // update
    // given
    Client expectedUpdated = new Client(3L, "aaa", "eee", null, LocalDate.now(), LocalDate.now());
    String nameExpectedUpdated = expectedUpdated.getName();
    String codeNameExpectedUpdated = expectedUpdated.getCodeName();
    Long keyAccountExpectedUpdated = expectedUpdated.getKeyAccount();
    LocalDate createDateExpectedUpdated = expectedUpdated.getCreateDate();
    LocalDate lastModificationDateExpectedUpdated = expectedUpdated.getLastModificationDate();
    // when
    Client actualUpdated = clientJpaRepository.update(expectedUpdated);
    String nameActualUpdated = actualUpdated.getName();
    String codeNameActualUpdated = actualUpdated.getCodeName();
    Long keyAccountActualUpdated = actualUpdated.getKeyAccount();
    LocalDate createDateActualUpdated = actualUpdated.getCreateDate();
    LocalDate lastModificationDateActualUpdated = actualUpdated.getLastModificationDate();
    // then
    assertEquals(nameExpectedUpdated, nameActualUpdated);
    assertEquals(codeNameExpectedUpdated, codeNameActualUpdated);
    assertEquals(keyAccountExpectedUpdated, keyAccountActualUpdated);
    assertEquals(createDateExpectedUpdated, createDateActualUpdated);
    assertEquals(lastModificationDateExpectedUpdated, lastModificationDateActualUpdated);

    // delete
    // given
    boolean expectedDeleted = true;
    // when
    boolean actualDeleted = clientJpaRepository.delete(3L);
    // then
    assertEquals(expectedDeleted, actualDeleted);
  }
}
