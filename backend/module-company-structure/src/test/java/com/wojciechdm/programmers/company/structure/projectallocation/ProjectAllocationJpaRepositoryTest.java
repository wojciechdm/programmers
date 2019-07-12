package com.wojciechdm.programmers.company.structure.projectallocation;

import static org.junit.jupiter.api.Assertions.*;

import com.wojciechdm.programmers.company.structure.employee.EmployeeLevel;
import com.wojciechdm.programmers.company.structure.employee.EmployeeRole;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.MySQLContainer;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

class ProjectAllocationJpaRepositoryTest {

  private static ProjectAllocationJpaRepository projectAllocationJpaRepository;
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
    projectAllocationJpaRepository = new ProjectAllocationJpaRepository(entityManagerFactory);
  }

  @AfterAll
  static void cleanUp() {
    entityManagerFactory.close();
    container.close();
  }

  @Test
  void shouldCrudProject() {
    // save
    // given
    ProjectAllocation expectedSavedFirst =
        new ProjectAllocation(
            null, 1L, 1L, LocalDate.now(), null, 100, null, EmployeeRole.DEVELOPER, EmployeeLevel.JUNIOR, 1500);
    Long projectIdExpectedSavedFirst = expectedSavedFirst.getProjectId();
    Long employeeIdExpectedSavedFirst = expectedSavedFirst.getEmployeeId();
    LocalDate startDateExpectedSavedFirst = expectedSavedFirst.getStartDate();
    LocalDate endDateExpectedSavedFirst = expectedSavedFirst.getEndDate();
    Integer percentileWorkloadExpectedSavedFirst = expectedSavedFirst.getPercentileWorkload();
    Integer hoursPerMonthWorkloadExpectedSavedFirst = expectedSavedFirst.getHoursPerMonthWorkload();
    EmployeeRole roleExpectedSavedFirst = expectedSavedFirst.getRole();
    EmployeeLevel levelExpectedSavedFirst = expectedSavedFirst.getLevel();
    Integer rateMonthlyExpectedSavedFirst = expectedSavedFirst.getRateMonthly();
    // when
    ProjectAllocation actualSaved = projectAllocationJpaRepository.save(expectedSavedFirst);
    Long projectIdActualSaved = actualSaved.getProjectId();
    Long employeeIdActualSaved = actualSaved.getEmployeeId();
    LocalDate startDateActualSaved = actualSaved.getStartDate();
    LocalDate endDateActualSaved = actualSaved.getEndDate();
    Integer percentileWorkloadActualSaved = actualSaved.getPercentileWorkload();
    Integer hoursPerMonthWorkloadActualSaved = actualSaved.getHoursPerMonthWorkload();
    EmployeeRole roleActualSaved = actualSaved.getRole();
    EmployeeLevel levelActualSaved = actualSaved.getLevel();
    Integer rateMonthlyActualSaved = actualSaved.getRateMonthly();
    // then
    assertEquals(projectIdExpectedSavedFirst, projectIdActualSaved);
    assertEquals(employeeIdExpectedSavedFirst, employeeIdActualSaved);
    assertEquals(startDateExpectedSavedFirst, startDateActualSaved);
    assertEquals(endDateExpectedSavedFirst, endDateActualSaved);
    assertEquals(percentileWorkloadExpectedSavedFirst, percentileWorkloadActualSaved);
    assertEquals(hoursPerMonthWorkloadExpectedSavedFirst, hoursPerMonthWorkloadActualSaved);
    assertEquals(roleExpectedSavedFirst, roleActualSaved);
    assertEquals(levelExpectedSavedFirst, levelActualSaved);
    assertEquals(rateMonthlyExpectedSavedFirst, rateMonthlyActualSaved);

    // exists
    // given
    boolean expectedExists = true;
    // when
    boolean actualExists = projectAllocationJpaRepository.exists(1L);
    // then
    assertEquals(expectedExists, actualExists);

    // count
    // given
    long expectedCount = 1L;
    // when
    long actualCount = projectAllocationJpaRepository.count();
    // then
    assertEquals(expectedCount, actualCount);

    // fetch
    // when
    ProjectAllocation actualFetch = projectAllocationJpaRepository.fetch(1L).get();
    Long projectIdActualFetch = actualFetch.getProjectId();
    Long employeeIdActualFetch = actualFetch.getEmployeeId();
    LocalDate startDateActualFetch = actualFetch.getStartDate();
    LocalDate endDateActualFetch = actualFetch.getEndDate();
    Integer percentileWorkloadActualFetch = actualFetch.getPercentileWorkload();
    Integer hoursPerMonthWorkloadActualFetch = actualFetch.getHoursPerMonthWorkload();
    EmployeeRole roleActualFetch = actualFetch.getRole();
    EmployeeLevel levelActualFetch = actualFetch.getLevel();
    Integer rateMonthlyActualFetch = actualFetch.getRateMonthly();
    // then
    assertEquals(projectIdExpectedSavedFirst, projectIdActualFetch);
    assertEquals(employeeIdExpectedSavedFirst, employeeIdActualFetch);
    assertEquals(startDateExpectedSavedFirst, startDateActualFetch);
    assertEquals(endDateExpectedSavedFirst, endDateActualFetch);
    assertEquals(percentileWorkloadExpectedSavedFirst, percentileWorkloadActualFetch);
    assertEquals(hoursPerMonthWorkloadExpectedSavedFirst, hoursPerMonthWorkloadActualFetch);
    assertEquals(roleExpectedSavedFirst, roleActualFetch);
    assertEquals(levelExpectedSavedFirst, levelActualFetch);
    assertEquals(rateMonthlyExpectedSavedFirst, rateMonthlyActualFetch);

    // fetch by employee id
    // given
    ProjectAllocation expectedSavedSecond =
        new ProjectAllocation(
            null, 1L, 1L, LocalDate.now(), LocalDate.now(), 10, null, EmployeeRole.SCRUM_MASTER, EmployeeLevel.JUNIOR, 2500);
    projectAllocationJpaRepository.save(expectedSavedSecond);
    Long projectIdExpectedSavedSecond = expectedSavedSecond.getProjectId();
    Long employeeIdExpectedSavedSecond = expectedSavedSecond.getEmployeeId();
    LocalDate startDateExpectedSavedSecond = expectedSavedSecond.getStartDate();
    LocalDate endDateExpectedSavedSecond = expectedSavedSecond.getEndDate();
    Integer percentileWorkloadExpectedSavedSecond = expectedSavedSecond.getPercentileWorkload();
    Integer hoursPerMonthWorkloadExpectedSavedSecond =
        expectedSavedSecond.getHoursPerMonthWorkload();
    EmployeeRole roleExpectedSavedSecond = expectedSavedSecond.getRole();
    EmployeeLevel levelExpectedSavedSecond = expectedSavedSecond.getLevel();
    Integer rateMonthlyExpectedSavedSecond = expectedSavedSecond.getRateMonthly();
    // when
    List<ProjectAllocation> actualFetchByEmployee =
        projectAllocationJpaRepository.fetchByEmployeeId(1L);
    Long projectIdActualFirstFetchByEmployee = actualFetchByEmployee.get(0).getProjectId();
    Long employeeIdActualFirstFetchByEmployee = actualFetchByEmployee.get(0).getEmployeeId();
    LocalDate startDateActualFirstFetchByEmployee = actualFetchByEmployee.get(0).getStartDate();
    LocalDate endDateActualFirstFetchByEmployee = actualFetchByEmployee.get(0).getEndDate();
    Integer percentileWorkloadActualFirstFetchByEmployee =
        actualFetchByEmployee.get(0).getPercentileWorkload();
    Integer hoursPerMonthWorkloadActualFirstFetchByEmployee =
        actualFetchByEmployee.get(0).getHoursPerMonthWorkload();
    EmployeeRole roleActualFirstFetchByEmployee = actualFetchByEmployee.get(0).getRole();
    EmployeeLevel levelActualFirstFetchByEmployee = actualFetchByEmployee.get(0).getLevel();
    Integer rateMonthlyActualFirstFetchByEmployee = actualFetchByEmployee.get(0).getRateMonthly();
    Long projectIdActualSecondFetchByEmployee = actualFetchByEmployee.get(1).getProjectId();
    Long employeeIdActualSecondFetchByEmployee = actualFetchByEmployee.get(1).getEmployeeId();
    LocalDate startDateActualSecondFetchByEmployee = actualFetchByEmployee.get(1).getStartDate();
    LocalDate endDateActualSecondFetchByEmployee = actualFetchByEmployee.get(1).getEndDate();
    Integer percentileWorkloadActualSecondFetchByEmployee =
        actualFetchByEmployee.get(1).getPercentileWorkload();
    Integer hoursPerMonthWorkloadActualSecondFetchByEmployee =
        actualFetchByEmployee.get(1).getHoursPerMonthWorkload();
    EmployeeRole roleActualSecondFetchByEmployee = actualFetchByEmployee.get(1).getRole();
    EmployeeLevel levelActualSecondFetchByEmployee = actualFetchByEmployee.get(1).getLevel();
    Integer rateMonthlyActualSecondFetchByEmployee = actualFetchByEmployee.get(1).getRateMonthly();
    // then
    assertEquals(projectIdExpectedSavedFirst, projectIdActualFirstFetchByEmployee);
    assertEquals(employeeIdExpectedSavedFirst, employeeIdActualFirstFetchByEmployee);
    assertEquals(startDateExpectedSavedFirst, startDateActualFirstFetchByEmployee);
    assertEquals(endDateExpectedSavedFirst, endDateActualFirstFetchByEmployee);
    assertEquals(
        percentileWorkloadExpectedSavedFirst, percentileWorkloadActualFirstFetchByEmployee);
    assertEquals(
        hoursPerMonthWorkloadExpectedSavedFirst, hoursPerMonthWorkloadActualFirstFetchByEmployee);
    assertEquals(roleExpectedSavedFirst, roleActualFirstFetchByEmployee);
    assertEquals(levelExpectedSavedFirst, levelActualFirstFetchByEmployee);
    assertEquals(rateMonthlyExpectedSavedFirst, rateMonthlyActualFirstFetchByEmployee);
    assertEquals(projectIdExpectedSavedSecond, projectIdActualSecondFetchByEmployee);
    assertEquals(employeeIdExpectedSavedSecond, employeeIdActualSecondFetchByEmployee);
    assertEquals(startDateExpectedSavedSecond, startDateActualSecondFetchByEmployee);
    assertEquals(endDateExpectedSavedSecond, endDateActualSecondFetchByEmployee);
    assertEquals(
        percentileWorkloadExpectedSavedSecond, percentileWorkloadActualSecondFetchByEmployee);
    assertEquals(
        hoursPerMonthWorkloadExpectedSavedSecond, hoursPerMonthWorkloadActualSecondFetchByEmployee);
    assertEquals(roleExpectedSavedSecond, roleActualSecondFetchByEmployee);
    assertEquals(levelExpectedSavedSecond, levelActualSecondFetchByEmployee);
    assertEquals(rateMonthlyExpectedSavedSecond, rateMonthlyActualSecondFetchByEmployee);

    // fetch by client id
    // when
    List<ProjectAllocation> actualFetchByProject =
        projectAllocationJpaRepository.fetchByProjectId(1L);
    Long projectIdActualFirstFetchByProject = actualFetchByProject.get(0).getProjectId();
    Long employeeIdActualFirstFetchByProject = actualFetchByProject.get(0).getEmployeeId();
    LocalDate startDateActualFirstFetchByProject = actualFetchByProject.get(0).getStartDate();
    LocalDate endDateActualFirstFetchByProject = actualFetchByProject.get(0).getEndDate();
    Integer percentileWorkloadActualFirstFetchByProject =
        actualFetchByProject.get(0).getPercentileWorkload();
    Integer hoursPerMonthWorkloadActualFirstFetchByProject =
        actualFetchByProject.get(0).getHoursPerMonthWorkload();
    EmployeeRole roleActualFirstFetchByProject = actualFetchByProject.get(0).getRole();
    EmployeeLevel levelActualFirstFetchByProject = actualFetchByProject.get(0).getLevel();
    Integer rateMonthlyActualFirstFetchByProject = actualFetchByProject.get(0).getRateMonthly();
    Long projectIdActualSecondFetchByProject = actualFetchByProject.get(1).getProjectId();
    Long employeeIdActualSecondFetchByProject = actualFetchByProject.get(1).getEmployeeId();
    LocalDate startDateActualSecondFetchByProject = actualFetchByProject.get(1).getStartDate();
    LocalDate endDateActualSecondFetchByProject = actualFetchByProject.get(1).getEndDate();
    Integer percentileWorkloadActualSecondFetchByProject =
        actualFetchByProject.get(1).getPercentileWorkload();
    Integer hoursPerMonthWorkloadActualSecondFetchByProject =
        actualFetchByProject.get(1).getHoursPerMonthWorkload();
    EmployeeRole roleActualSecondFetchByProject = actualFetchByProject.get(1).getRole();
    EmployeeLevel levelActualSecondFetchByProject = actualFetchByProject.get(1).getLevel();
    Integer rateMonthlyActualSecondFetchByProject = actualFetchByProject.get(1).getRateMonthly();
    // then
    assertEquals(projectIdExpectedSavedFirst, projectIdActualFirstFetchByProject);
    assertEquals(employeeIdExpectedSavedFirst, employeeIdActualFirstFetchByProject);
    assertEquals(startDateExpectedSavedFirst, startDateActualFirstFetchByProject);
    assertEquals(endDateExpectedSavedFirst, endDateActualFirstFetchByProject);
    assertEquals(percentileWorkloadExpectedSavedFirst, percentileWorkloadActualFirstFetchByProject);
    assertEquals(
        hoursPerMonthWorkloadExpectedSavedFirst, hoursPerMonthWorkloadActualFirstFetchByProject);
    assertEquals(roleExpectedSavedFirst, roleActualFirstFetchByProject);
    assertEquals(levelExpectedSavedFirst, levelActualFirstFetchByProject);
    assertEquals(rateMonthlyExpectedSavedFirst, rateMonthlyActualFirstFetchByProject);
    assertEquals(projectIdExpectedSavedSecond, projectIdActualSecondFetchByProject);
    assertEquals(employeeIdExpectedSavedSecond, employeeIdActualSecondFetchByProject);
    assertEquals(startDateExpectedSavedSecond, startDateActualSecondFetchByProject);
    assertEquals(endDateExpectedSavedSecond, endDateActualSecondFetchByProject);
    assertEquals(
        percentileWorkloadExpectedSavedSecond, percentileWorkloadActualSecondFetchByProject);
    assertEquals(
        hoursPerMonthWorkloadExpectedSavedSecond, hoursPerMonthWorkloadActualSecondFetchByProject);
    assertEquals(roleExpectedSavedSecond, roleActualSecondFetchByProject);
    assertEquals(levelExpectedSavedSecond, levelActualSecondFetchByProject);
    assertEquals(rateMonthlyExpectedSavedSecond, rateMonthlyActualSecondFetchByProject);

    // fetch all
    // when
    List<ProjectAllocation> actualFetchAll =
        projectAllocationJpaRepository.fetchAll(1, 1, ProjectAllocationSortProperty.ID, false);
    Long projectIdActualFetchAll = actualFetchAll.get(0).getProjectId();
    Long employeeIdActualFetchAll = actualFetchAll.get(0).getEmployeeId();
    LocalDate startDateActualFetchAll = actualFetchAll.get(0).getStartDate();
    LocalDate endDateActualFetchAll = actualFetchAll.get(0).getEndDate();
    Integer percentileWorkloadActualFetchAll = actualFetchAll.get(0).getPercentileWorkload();
    Integer hoursPerMonthWorkloadActualFetchAll = actualFetchAll.get(0).getHoursPerMonthWorkload();
    EmployeeRole roleActualFetchAll = actualFetchAll.get(0).getRole();
    EmployeeLevel levelActualFetchAll = actualFetchAll.get(0).getLevel();
    Integer rateMonthlyActualFetchAll = actualFetchAll.get(0).getRateMonthly();
    // then
    assertEquals(projectIdExpectedSavedFirst, projectIdActualFetchAll);
    assertEquals(employeeIdExpectedSavedFirst, employeeIdActualFetchAll);
    assertEquals(startDateExpectedSavedFirst, startDateActualFetchAll);
    assertEquals(endDateExpectedSavedFirst, endDateActualFetchAll);
    assertEquals(percentileWorkloadExpectedSavedFirst, percentileWorkloadActualFetchAll);
    assertEquals(hoursPerMonthWorkloadExpectedSavedFirst, hoursPerMonthWorkloadActualFetchAll);
    assertEquals(roleExpectedSavedFirst, roleActualFetchAll);
    assertEquals(levelExpectedSavedFirst, levelActualFetchAll);
    assertEquals(rateMonthlyExpectedSavedFirst, rateMonthlyActualFetchAll);

    // update
    // given
    ProjectAllocation expectedUpdated =
        new ProjectAllocation(1L, 1L, 1L, LocalDate.now(), null, 90, null, EmployeeRole.DEVELOPER, EmployeeLevel.JUNIOR, 1800);
    Long projectIdExpectedUpdated = expectedUpdated.getProjectId();
    Long employeeIdExpectedUpdated = expectedUpdated.getEmployeeId();
    LocalDate startDateExpectedUpdated = expectedUpdated.getStartDate();
    LocalDate endDateExpectedUpdated = expectedUpdated.getEndDate();
    Integer percentileWorkloadExpectedUpdated = expectedUpdated.getPercentileWorkload();
    Integer hoursPerMonthWorkloadExpectedUpdated = expectedUpdated.getHoursPerMonthWorkload();
    EmployeeRole roleExpectedUpdated = expectedUpdated.getRole();
    EmployeeLevel levelExpectedUpdated = expectedUpdated.getLevel();
    Integer rateMonthlyExpectedUpdated = expectedUpdated.getRateMonthly();
    // when
    ProjectAllocation actualUpdated = projectAllocationJpaRepository.update(expectedUpdated);
    Long projectIdActualUpdated = actualUpdated.getProjectId();
    Long employeeIdActualUpdated = actualUpdated.getEmployeeId();
    LocalDate startDateActualUpdated = actualUpdated.getStartDate();
    LocalDate endDateActualUpdated = actualUpdated.getEndDate();
    Integer percentileWorkloadActualUpdated = actualUpdated.getPercentileWorkload();
    Integer hoursPerMonthWorkloadActualUpdated = actualUpdated.getHoursPerMonthWorkload();
    EmployeeRole roleActualUpdated = actualUpdated.getRole();
    EmployeeLevel levelActualUpdated = actualUpdated.getLevel();
    Integer rateMonthlyActualUpdated = actualUpdated.getRateMonthly();
    // then
    assertEquals(projectIdExpectedUpdated, projectIdActualUpdated);
    assertEquals(employeeIdExpectedUpdated, employeeIdActualUpdated);
    assertEquals(startDateExpectedUpdated, startDateActualUpdated);
    assertEquals(endDateExpectedUpdated, endDateActualUpdated);
    assertEquals(percentileWorkloadExpectedUpdated, percentileWorkloadActualUpdated);
    assertEquals(hoursPerMonthWorkloadExpectedUpdated, hoursPerMonthWorkloadActualUpdated);
    assertEquals(roleExpectedUpdated, roleActualUpdated);
    assertEquals(levelExpectedUpdated, levelActualUpdated);
    assertEquals(rateMonthlyExpectedUpdated, rateMonthlyActualUpdated);

    // delete
    // given
    boolean expectedDeleted = true;
    // when
    boolean actualDeleted = projectAllocationJpaRepository.delete(1L);
    // then
    assertEquals(expectedDeleted, actualDeleted);
  }
}
