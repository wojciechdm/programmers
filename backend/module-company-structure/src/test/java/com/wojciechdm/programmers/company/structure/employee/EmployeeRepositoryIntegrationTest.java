package com.wojciechdm.programmers.company.structure.employee;

import static com.wojciechdm.programmers.company.structure.employee.EmployeeLevel.*;
import static com.wojciechdm.programmers.company.structure.employee.EmployeeRole.*;
import static com.wojciechdm.programmers.company.structure.employee.EmployeeStatus.*;
import static org.junit.jupiter.api.Assertions.*;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.MySQLContainer;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

class EmployeeRepositoryIntegrationTest {

  private EmployeeRepository employeeRepository;
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
    employeeRepository = new EmployeeRepository(dataSource);
  }

  @Test
  void shouldCrudEmployee() {
    // save
    // given
    Employee expectedSaved =
        new Employee(
            null,
            "qqq",
            "zzz",
            null,
            null,
            CANDIDATE,
            Map.of(DEVELOPER, JUNIOR, DB_SPECIALIST, SENIOR));
    String firstNameExpectedSaved = expectedSaved.getFirstName();
    String lastNameExpectedSaved = expectedSaved.getLastName();
    String peselExpectedSaved = expectedSaved.getPesel();
    LocalDate employmentDateExpectedSaved = expectedSaved.getEmploymentDate();
    EmployeeStatus statusExpectedSaved = expectedSaved.getStatus();
    Map<EmployeeRole, EmployeeLevel> rolesExpectedSaved = expectedSaved.getRoles();
    // when
    Employee actualSaved = employeeRepository.save(expectedSaved);
    String firstNameActualSaved = actualSaved.getFirstName();
    String lastNameActualSaved = actualSaved.getLastName();
    String peselActualSaved = actualSaved.getPesel();
    LocalDate employmentDateActualSaved = actualSaved.getEmploymentDate();
    EmployeeStatus statusActualSaved = actualSaved.getStatus();
    Map<EmployeeRole, EmployeeLevel> rolesActualSaved = actualSaved.getRoles();
    // then
    assertEquals(firstNameExpectedSaved, firstNameActualSaved);
    assertEquals(lastNameExpectedSaved, lastNameActualSaved);
    assertEquals(peselExpectedSaved, peselActualSaved);
    assertEquals(employmentDateExpectedSaved, employmentDateActualSaved);
    assertEquals(statusExpectedSaved, statusActualSaved);
    assertEquals(rolesExpectedSaved, rolesActualSaved);

    // exists
    // given
    boolean expectedExists = true;
    // when
    boolean actualExists = employeeRepository.exists(2L);
    // then
    assertEquals(expectedExists, actualExists);

    // count
    // given
    long expectedCount = 2L;
    // when
    long actualCount = employeeRepository.count();
    // then
    assertEquals(expectedCount, actualCount);

    // fetch
    // when
    Employee actualFetch = employeeRepository.fetch(2L).get();
    String firstNameActualFetch = actualFetch.getFirstName();
    String lastNameActualFetch = actualFetch.getLastName();
    String peselActualFetch = actualFetch.getPesel();
    LocalDate employmentDateActualFetch = actualFetch.getEmploymentDate();
    EmployeeStatus statusActualFetch = actualFetch.getStatus();
    Map<EmployeeRole, EmployeeLevel> rolesActualFetch = actualFetch.getRoles();
    // then
    assertEquals(firstNameExpectedSaved, firstNameActualFetch);
    assertEquals(lastNameExpectedSaved, lastNameActualFetch);
    assertEquals(peselExpectedSaved, peselActualFetch);
    assertEquals(employmentDateExpectedSaved, employmentDateActualFetch);
    assertEquals(statusExpectedSaved, statusActualFetch);
    assertEquals(rolesExpectedSaved, rolesActualFetch);

    // fetch all
    // when
    List<Employee> actualFetchAll =
        employeeRepository.fetchAll(2, 1, EmployeeSortProperty.ID, false);
    String firstNameActualFetchAll = actualFetchAll.get(0).getFirstName();
    String lastNameActualFetchAll = actualFetchAll.get(0).getLastName();
    String peselActualFetchAll = actualFetchAll.get(0).getPesel();
    LocalDate employmentDateActualFetchAll = actualFetchAll.get(0).getEmploymentDate();
    EmployeeStatus statusActualFetchAll = actualFetchAll.get(0).getStatus();
    Map<EmployeeRole, EmployeeLevel> rolesActualFetchAll = actualFetchAll.get(0).getRoles();
    // then
    assertEquals(firstNameExpectedSaved, firstNameActualFetchAll);
    assertEquals(lastNameExpectedSaved, lastNameActualFetchAll);
    assertEquals(peselExpectedSaved, peselActualFetchAll);
    assertEquals(employmentDateExpectedSaved, employmentDateActualFetchAll);
    assertEquals(statusExpectedSaved, statusActualFetchAll);
    assertEquals(rolesExpectedSaved, rolesActualFetchAll);

    // update
    // given
    Employee expectedUpdated =
        new Employee(
            2L,
            "qqq",
            "zzz",
            "56363653357",
            LocalDate.of(2019, 4, 24),
            EMPLOYED,
            Map.of(SCRUM_MASTER, PROFESSIONAL, ANALYST, JUNIOR));
    String firstNameExpectedUpdated = expectedUpdated.getFirstName();
    String lastNameExpectedUpdated = expectedUpdated.getLastName();
    String peselExpectedUpdated = expectedUpdated.getPesel();
    LocalDate employmentDateExpectedUpdated = expectedUpdated.getEmploymentDate();
    EmployeeStatus statusExpectedUpdated = expectedUpdated.getStatus();
    Map<EmployeeRole, EmployeeLevel> rolesExpectedUpdated = expectedUpdated.getRoles();
    // when
    Employee actualUpdated = employeeRepository.update(expectedUpdated);
    String firstNameActualUpdated = actualUpdated.getFirstName();
    String lastNameActualUpdated = actualUpdated.getLastName();
    String peselActualUpdated = actualUpdated.getPesel();
    LocalDate employmentDateActualUpdated = actualUpdated.getEmploymentDate();
    EmployeeStatus statusActualUpdated = actualUpdated.getStatus();
    Map<EmployeeRole, EmployeeLevel> rolesActualUpdated = actualUpdated.getRoles();
    // then
    assertEquals(firstNameExpectedUpdated, firstNameActualUpdated);
    assertEquals(lastNameExpectedUpdated, lastNameActualUpdated);
    assertEquals(peselExpectedUpdated, peselActualUpdated);
    assertEquals(employmentDateExpectedUpdated, employmentDateActualUpdated);
    assertEquals(statusExpectedUpdated, statusActualUpdated);
    assertEquals(rolesExpectedUpdated, rolesActualUpdated);

    // delete
    // given
    boolean expectedDeleted = true;
    // when
    boolean actualDeleted = employeeRepository.delete(2L);
    // then
    assertEquals(expectedDeleted, actualDeleted);
  }
}
