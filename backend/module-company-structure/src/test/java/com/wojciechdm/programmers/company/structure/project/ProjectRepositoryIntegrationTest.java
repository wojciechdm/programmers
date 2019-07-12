package com.wojciechdm.programmers.company.structure.project;

import static org.junit.jupiter.api.Assertions.*;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.MySQLContainer;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

class ProjectRepositoryIntegrationTest {

  private ProjectRepository projectRepository;
  private MySQLContainer container = new MySQLContainer();
  private MysqlDataSource dataSource = new MysqlDataSource();

  @BeforeEach
  void setUp() {
    container.withInitScript("database_model.sql");
    container.start();
    dataSource.setUrl(container.getJdbcUrl());
    dataSource.setUser(container.getUsername());
    dataSource.setPassword(container.getPassword());
    try {
      dataSource.setUseSSL(false);
      dataSource.setServerTimezone("Europe/Warsaw");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    projectRepository = new ProjectRepository(dataSource);
  }

  @Test
  void shouldCrudProject() {
    // save
    // given
    Project expectedSavedFirst =
        new Project(
            null,
            "qqq",
            "zzz123",
            LocalDate.of(2018, 12, 13),
            null,
            ProjectStatus.ACTIVE,
            LocalDate.now(),
            LocalDate.now(),
            1L);
    String nameExpectedSavedFirst = expectedSavedFirst.getName();
    String codeNameExpectedSavedFirst = expectedSavedFirst.getCodeName();
    LocalDate startDateExpectedSavedFirst = expectedSavedFirst.getStartDate();
    LocalDate endDateExpectedSavedFirst = expectedSavedFirst.getEndDate();
    ProjectStatus statusExpectedSavedFirst = expectedSavedFirst.getStatus();
    LocalDate createDateExpectedSavedFirst = expectedSavedFirst.getCreateDate();
    LocalDate lastModificationDateExpectedSavedFirst = expectedSavedFirst.getLastModificationDate();
    Long clientIdExpectedSavedFirst = expectedSavedFirst.getClientId();
    // when
    Project actualSaved = projectRepository.save(expectedSavedFirst);
    String nameActualSaved = actualSaved.getName();
    String codeNameActualSaved = actualSaved.getCodeName();
    LocalDate startDateActualSaved = actualSaved.getStartDate();
    LocalDate endDateActualSaved = actualSaved.getEndDate();
    ProjectStatus statusActualSaved = actualSaved.getStatus();
    LocalDate createDateActualSaved = actualSaved.getCreateDate();
    LocalDate lastModificationDateActualSaved = actualSaved.getLastModificationDate();
    Long clientIdActualSaved = actualSaved.getClientId();
    // then
    assertEquals(nameExpectedSavedFirst, nameActualSaved);
    assertEquals(codeNameExpectedSavedFirst, codeNameActualSaved);
    assertEquals(startDateExpectedSavedFirst, startDateActualSaved);
    assertEquals(endDateExpectedSavedFirst, endDateActualSaved);
    assertEquals(statusExpectedSavedFirst, statusActualSaved);
    assertEquals(createDateExpectedSavedFirst, createDateActualSaved);
    assertEquals(lastModificationDateExpectedSavedFirst, lastModificationDateActualSaved);
    assertEquals(clientIdExpectedSavedFirst, clientIdActualSaved);

    // exists
    // given
    boolean expectedExists = true;
    // when
    boolean actualExists = projectRepository.exists(2L);
    // then
    assertEquals(expectedExists, actualExists);

    // count
    // given
    long expectedCount = 2L;
    // when
    long actualCount = projectRepository.count();
    // then
    assertEquals(expectedCount, actualCount);

    // fetch
    // when
    Project actualFetch = projectRepository.fetch(2L).get();
    String nameActualFetch = actualFetch.getName();
    String codeNameActualFetch = actualFetch.getCodeName();
    LocalDate startDateActualFetch = actualFetch.getStartDate();
    LocalDate endDateActualFetch = actualFetch.getEndDate();
    ProjectStatus statusActualFetch = actualFetch.getStatus();
    LocalDate createDateActualFetch = actualFetch.getCreateDate();
    LocalDate lastModificationDateActualFetch = actualFetch.getLastModificationDate();
    Long clientIdActualFetch = actualFetch.getClientId();
    // then
    assertEquals(nameExpectedSavedFirst, nameActualFetch);
    assertEquals(codeNameExpectedSavedFirst, codeNameActualFetch);
    assertEquals(startDateExpectedSavedFirst, startDateActualFetch);
    assertEquals(endDateExpectedSavedFirst, endDateActualFetch);
    assertEquals(statusExpectedSavedFirst, statusActualFetch);
    assertEquals(createDateExpectedSavedFirst, createDateActualFetch);
    assertEquals(lastModificationDateExpectedSavedFirst, lastModificationDateActualFetch);
    assertEquals(clientIdExpectedSavedFirst, clientIdActualFetch);

    // fetch by client id
    // given
    Project expectedSavedSecond =
        new Project(
            null,
            "yyy",
            "www123",
            LocalDate.of(2018, 10, 11),
            null,
            ProjectStatus.COMPLETED,
            LocalDate.now(),
            LocalDate.now(),
            1L);
    projectRepository.save(expectedSavedSecond);
    String nameExpectedSavedSecond = expectedSavedSecond.getName();
    String codeNameExpectedSavedSecond = expectedSavedSecond.getCodeName();
    LocalDate startDateExpectedSavedSecond = expectedSavedSecond.getStartDate();
    LocalDate endDateExpectedSavedSecond = expectedSavedSecond.getEndDate();
    ProjectStatus statusExpectedSavedSecond = expectedSavedSecond.getStatus();
    LocalDate createDateExpectedSavedSecond = expectedSavedSecond.getCreateDate();
    LocalDate lastModificationDateExpectedSavedSecond =
        expectedSavedSecond.getLastModificationDate();
    Long clientIdExpectedSavedSecond = expectedSavedSecond.getClientId();
    // when
    List<Project> actualFetchByClient = projectRepository.fetchByClientId(1L);
    String nameActualFirstFetchByClient = actualFetchByClient.get(0).getName();
    String codeNameActualFirstFetchByClient = actualFetchByClient.get(0).getCodeName();
    LocalDate startDateActualFirstFetchByClient = actualFetchByClient.get(0).getStartDate();
    LocalDate endDateActualFirstFetchByClient = actualFetchByClient.get(0).getEndDate();
    ProjectStatus statusActualFirstFetchByClient = actualFetchByClient.get(0).getStatus();
    LocalDate createDateActualFirstFetchByClient = actualFetchByClient.get(0).getCreateDate();
    LocalDate lastModificationDateActualFirstFetchByClient =
        actualFetchByClient.get(0).getLastModificationDate();
    Long clientIdActualFirstFetchByClient = actualFetchByClient.get(0).getClientId();
    String nameActualSecondFetchByClient = actualFetchByClient.get(1).getName();
    String codeNameActualSecondFetchByClient = actualFetchByClient.get(1).getCodeName();
    LocalDate startDateActualSecondFetchByClient = actualFetchByClient.get(1).getStartDate();
    LocalDate endDateActualSecondFetchByClient = actualFetchByClient.get(1).getEndDate();
    ProjectStatus statusActualSecondFetchByClient = actualFetchByClient.get(1).getStatus();
    LocalDate createDateActualSecondFetchByClient = actualFetchByClient.get(1).getCreateDate();
    LocalDate lastModificationDateActualSecondFetchByClient =
        actualFetchByClient.get(1).getLastModificationDate();
    Long clientIdActualSecondFetchByClient = actualFetchByClient.get(1).getClientId();
    // then
    assertEquals(nameExpectedSavedFirst, nameActualFirstFetchByClient);
    assertEquals(codeNameExpectedSavedFirst, codeNameActualFirstFetchByClient);
    assertEquals(startDateExpectedSavedFirst, startDateActualFirstFetchByClient);
    assertEquals(endDateExpectedSavedFirst, endDateActualFirstFetchByClient);
    assertEquals(statusExpectedSavedFirst, statusActualFirstFetchByClient);
    assertEquals(createDateExpectedSavedFirst, createDateActualFirstFetchByClient);
    assertEquals(
        lastModificationDateExpectedSavedFirst, lastModificationDateActualFirstFetchByClient);
    assertEquals(clientIdExpectedSavedFirst, clientIdActualFirstFetchByClient);
    assertEquals(nameExpectedSavedSecond, nameActualSecondFetchByClient);
    assertEquals(codeNameExpectedSavedSecond, codeNameActualSecondFetchByClient);
    assertEquals(startDateExpectedSavedSecond, startDateActualSecondFetchByClient);
    assertEquals(endDateExpectedSavedSecond, endDateActualSecondFetchByClient);
    assertEquals(statusExpectedSavedSecond, statusActualSecondFetchByClient);
    assertEquals(createDateExpectedSavedSecond, createDateActualSecondFetchByClient);
    assertEquals(
        lastModificationDateExpectedSavedSecond, lastModificationDateActualSecondFetchByClient);
    assertEquals(clientIdExpectedSavedSecond, clientIdActualSecondFetchByClient);

    // fetch all
    // when
    List<Project> actualFetchAll = projectRepository.fetchAll(2, 1, ProjectSortProperty.ID, false);
    String nameActualFetchAll = actualFetchAll.get(0).getName();
    String codeNameActualFetchAll = actualFetchAll.get(0).getCodeName();
    LocalDate startDateActualFetchAll = actualFetchAll.get(0).getStartDate();
    LocalDate endDateActualFetchAll = actualFetchAll.get(0).getEndDate();
    ProjectStatus statusActualFetchAll = actualFetchAll.get(0).getStatus();
    LocalDate createDateActualFetchAll = actualFetchAll.get(0).getCreateDate();
    LocalDate lastModificationDateActualFetchAll = actualFetchAll.get(0).getLastModificationDate();
    Long clientIdActualFetchAll = actualFetchAll.get(0).getClientId();
    // then
    assertEquals(nameExpectedSavedFirst, nameActualFetchAll);
    assertEquals(codeNameExpectedSavedFirst, codeNameActualFetchAll);
    assertEquals(startDateExpectedSavedFirst, startDateActualFetchAll);
    assertEquals(endDateExpectedSavedFirst, endDateActualFetchAll);
    assertEquals(statusExpectedSavedFirst, statusActualFetchAll);
    assertEquals(createDateExpectedSavedFirst, createDateActualFetchAll);
    assertEquals(lastModificationDateExpectedSavedFirst, lastModificationDateActualFetchAll);
    assertEquals(clientIdExpectedSavedFirst, clientIdActualFetchAll);

    // update
    // given
    Project expectedUpdated =
        new Project(
            2L,
            "uuu",
            "ppp123",
            LocalDate.of(2018, 10, 10),
            LocalDate.of(2019, 10, 20),
            ProjectStatus.SUSPENDED,
            LocalDate.now(),
            LocalDate.now(),
            1L);
    String nameExpectedUpdated = expectedUpdated.getName();
    String codeNameExpectedUpdated = expectedUpdated.getCodeName();
    LocalDate startDateExpectedUpdated = expectedUpdated.getStartDate();
    LocalDate endDateExpectedUpdated = expectedUpdated.getEndDate();
    ProjectStatus statusExpectedUpdated = expectedUpdated.getStatus();
    LocalDate createDateExpectedUpdated = expectedUpdated.getCreateDate();
    LocalDate lastModificationDateExpectedUpdated = expectedUpdated.getLastModificationDate();
    Long clientIdExpectedUpdated = expectedUpdated.getClientId();
    // when
    Project actualUpdated = projectRepository.update(expectedUpdated);
    String nameActualUpdated = actualUpdated.getName();
    String codeNameActualUpdated = actualUpdated.getCodeName();
    LocalDate startDateActualUpdated = actualUpdated.getStartDate();
    LocalDate endDateActualUpdated = actualUpdated.getEndDate();
    ProjectStatus statusActualUpdated = actualUpdated.getStatus();
    LocalDate createDateActualUpdated = actualUpdated.getCreateDate();
    LocalDate lastModificationDateActualUpdated = actualUpdated.getLastModificationDate();
    Long clientIdActualUpdated = actualUpdated.getClientId();
    // then
    assertEquals(nameExpectedUpdated, nameActualUpdated);
    assertEquals(codeNameExpectedUpdated, codeNameActualUpdated);
    assertEquals(startDateExpectedUpdated, startDateActualUpdated);
    assertEquals(endDateExpectedUpdated, endDateActualUpdated);
    assertEquals(statusExpectedUpdated, statusActualUpdated);
    assertEquals(createDateExpectedUpdated, createDateActualUpdated);
    assertEquals(lastModificationDateExpectedUpdated, lastModificationDateActualUpdated);
    assertEquals(clientIdExpectedUpdated, clientIdActualUpdated);

    // delete
    // given
    boolean expectedDeleted = true;
    // when
    boolean actualDeleted = projectRepository.delete(2L);
    // then
    assertEquals(expectedDeleted, actualDeleted);
  }
}
