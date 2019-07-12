package com.wojciechdm.programmers.company.structure.projectallocation;

import static com.wojciechdm.programmers.company.structure.employee.EmployeeLevel.*;
import static com.wojciechdm.programmers.company.structure.employee.EmployeeRole.*;
import static com.wojciechdm.programmers.company.structure.employee.EmployeeStatus.*;
import static com.wojciechdm.programmers.company.structure.project.ProjectStatus.*;
import static com.wojciechdm.programmers.company.structure.projectallocation.ProjectAllocationRuleViolation.*;
import static org.junit.jupiter.api.Assertions.*;

import com.wojciechdm.programmers.company.structure.employee.EmployeeDisplayDto;
import com.wojciechdm.programmers.company.structure.project.ProjectDisplayDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

class ProjectAllocationValidatorTest {

  private ProjectAllocationValidator projectAllocationValidator;

  @BeforeEach
  void setUp() {
    projectAllocationValidator = new ProjectAllocationValidator();
  }

  @ParameterizedTest
  @MethodSource("validProjectAllocationsForSave")
  void shouldSayProjectAllocationForSaveIsValid(
      ProjectAllocationSaveDto projectAllocation,
      List<ProjectAllocationDisplayDto> projectAllocations,
      EmployeeDisplayDto employee,
      ProjectDisplayDto project) {
    // given
    boolean expected = true;
    // when
    boolean actual =
        projectAllocationValidator
            .validate(projectAllocation, projectAllocations, employee, project)
            .isValid();
    // then
    assertEquals(expected, actual);
  }

  @ParameterizedTest
  @MethodSource("validProjectAllocationsForUpdate")
  void shouldSayProjectAllocationForUpdateIsValid(
      ProjectAllocationSaveDto projectAllocation,
      List<ProjectAllocationDisplayDto> projectAllocations,
      EmployeeDisplayDto employee,
      ProjectDisplayDto project) {
    // given
    boolean expected = true;
    // when
    boolean actual =
        projectAllocationValidator
            .validate(4L, projectAllocation, projectAllocations, employee, project)
            .isValid();
    // then
    assertEquals(expected, actual);
  }

  @ParameterizedTest
  @MethodSource("invalidProjectAllocationsForSave")
  void shouldReturnProjectAllocationForSaveRulesViolations(
      ProjectAllocationSaveDto projectAllocation,
      List<ProjectAllocationDisplayDto> projectAllocations,
      EmployeeDisplayDto employee,
      ProjectDisplayDto project,
      EnumMap<ProjectAllocationRuleViolation, Boolean> expected) {
    // when
    EnumMap<ProjectAllocationRuleViolation, Boolean> actual =
        projectAllocationValidator
            .validate(projectAllocation, projectAllocations, employee, project)
            .getRulesViolations();
    // then
    assertEquals(expected, actual);
  }

  @ParameterizedTest
  @MethodSource("invalidProjectAllocationsForUpdate")
  void shouldReturnProjectAllocationForUpdateRulesViolations(
      ProjectAllocationSaveDto projectAllocation,
      List<ProjectAllocationDisplayDto> projectAllocations,
      EmployeeDisplayDto employee,
      ProjectDisplayDto project,
      EnumMap<ProjectAllocationRuleViolation, Boolean> expected) {
    // when
    EnumMap<ProjectAllocationRuleViolation, Boolean> actual =
        projectAllocationValidator
            .validate(5L, projectAllocation, projectAllocations, employee, project)
            .getRulesViolations();
    // then
    assertEquals(expected, actual);
  }

  static Stream<Arguments> validProjectAllocationsForSave() {
    return Stream.of(
        Arguments.of(
            new ProjectAllocationSaveDto(
                1L,
                1L,
                LocalDate.now().minusMonths(10),
                LocalDate.now().plusMonths(10),
                40,
                null,
                TESTER,
                JUNIOR,
                2000),
            List.of(
                new ProjectAllocationDisplayDto(
                    1L,
                    1L,
                    1L,
                    LocalDate.now().minusMonths(11),
                    LocalDate.now().plusMonths(11),
                    null,
                    60,
                    SCRUM_MASTER,
                    JUNIOR,
                    2000),
                new ProjectAllocationDisplayDto(
                    1L,
                    2L,
                    1L,
                    LocalDate.now().minusMonths(11),
                    LocalDate.now().plusMonths(11),
                    20,
                    null,
                    TESTER,
                    JUNIOR,
                    2000),
                new ProjectAllocationDisplayDto(
                    1L,
                    3L,
                    1L,
                    LocalDate.now().minusMonths(11),
                    LocalDate.now().plusMonths(11),
                    10,
                    null,
                    TESTER,
                    JUNIOR,
                    2000)),
            new EmployeeDisplayDto(
                1L,
                "qqq",
                "ooo",
                "12345678901",
                LocalDate.now(),
                EMPLOYED,
                Map.of(TESTER, PROFESSIONAL, SCRUM_MASTER, SENIOR)),
            new ProjectDisplayDto(
                1L,
                "xxx",
                "ddd",
                LocalDate.now().minusMonths(10),
                LocalDate.now().plusMonths(11),
                ACTIVE,
                LocalDate.now(),
                LocalDate.now(),
                1L)));
  }

  static Stream<Arguments> validProjectAllocationsForUpdate() {
    return Stream.of(
        Arguments.of(
            new ProjectAllocationSaveDto(
                1L,
                1L,
                LocalDate.now().minusMonths(10),
                LocalDate.now().plusMonths(10),
                40,
                null,
                TESTER,
                JUNIOR,
                2000),
            List.of(
                new ProjectAllocationDisplayDto(
                    1L,
                    1L,
                    1L,
                    LocalDate.now().minusMonths(11),
                    LocalDate.now().plusMonths(11),
                    null,
                    60,
                    SCRUM_MASTER,
                    JUNIOR,
                    2000),
                new ProjectAllocationDisplayDto(
                    2L,
                    2L,
                    1L,
                    LocalDate.now().minusMonths(11),
                    LocalDate.now().plusMonths(11),
                    20,
                    null,
                    TESTER,
                    JUNIOR,
                    2000),
                new ProjectAllocationDisplayDto(
                    3L,
                    3L,
                    1L,
                    LocalDate.now().minusMonths(11),
                    LocalDate.now().plusMonths(11),
                    10,
                    null,
                    TESTER,
                    JUNIOR,
                    2000),
                new ProjectAllocationDisplayDto(
                    4L,
                    3L,
                    1L,
                    LocalDate.now().minusMonths(11),
                    LocalDate.now().plusMonths(11),
                    90,
                    null,
                    DEVELOPER,
                    JUNIOR,
                    2000)),
            new EmployeeDisplayDto(
                1L,
                "qqq",
                "ooo",
                "12345678901",
                LocalDate.now(),
                EMPLOYED,
                Map.of(TESTER, PROFESSIONAL, SCRUM_MASTER, SENIOR, DEVELOPER, SENIOR)),
            new ProjectDisplayDto(
                1L,
                "xxx",
                "ddd",
                LocalDate.now().minusMonths(10),
                LocalDate.now().plusMonths(11),
                ACTIVE,
                LocalDate.now(),
                LocalDate.now(),
                1L)));
  }

  static Stream<Arguments> invalidProjectAllocationsForSave() {
    return Stream.of(
        Arguments.of(
            new ProjectAllocationSaveDto(
                1L,
                1L,
                LocalDate.now().minusMonths(12),
                LocalDate.now().plusMonths(12),
                40,
                20,
                TESTER,
                SENIOR,
                2000),
            List.of(
                new ProjectAllocationDisplayDto(
                    1L,
                    1L,
                    1L,
                    LocalDate.now().minusMonths(11),
                    LocalDate.now().plusMonths(11),
                    null,
                    164,
                    SCRUM_MASTER,
                    JUNIOR,
                    2000),
                new ProjectAllocationDisplayDto(
                    1L,
                    2L,
                    1L,
                    LocalDate.now().minusMonths(11),
                    LocalDate.now().plusMonths(11),
                    null,
                    60,
                    SCRUM_MASTER,
                    JUNIOR,
                    2000),
                new ProjectAllocationDisplayDto(
                    1L,
                    3L,
                    1L,
                    LocalDate.now().minusMonths(11),
                    LocalDate.now().plusMonths(11),
                    20,
                    null,
                    TESTER,
                    JUNIOR,
                    2000),
                new ProjectAllocationDisplayDto(
                    1L,
                    4L,
                    1L,
                    LocalDate.now().minusMonths(11),
                    LocalDate.now().plusMonths(11),
                    10,
                    null,
                    TESTER,
                    JUNIOR,
                    2000)),
            new EmployeeDisplayDto(
                1L,
                "qqq",
                "ooo",
                "12345678901",
                LocalDate.now(),
                EMPLOYED,
                Map.of(TESTER, PROFESSIONAL, SCRUM_MASTER, SENIOR)),
            new ProjectDisplayDto(
                1L,
                "xxx",
                "ddd",
                LocalDate.now().minusMonths(11),
                LocalDate.now().plusMonths(11),
                COMPLETED,
                LocalDate.now(),
                LocalDate.now(),
                1L),
            new EnumMap<>(
                Map.of(
                    INVALID_TIME_FRAME,
                    true,
                    INVALID_WORKLOADS_NUMBER,
                    true,
                    INVALID_ROLE,
                    true,
                    INVALID_PROJECT_STATUS,
                    true,
                    INVALID_ALLOCATED_PROJECTS_NUMBER,
                    true,
                    INVALID_DEVELOPER_ROLES_NUMBER,
                    false,
                    INVALID_MAX_WORKLOAD,
                    true))),
        Arguments.of(
            new ProjectAllocationSaveDto(
                1L,
                1L,
                LocalDate.now().minusMonths(12),
                LocalDate.now().plusMonths(12),
                40,
                20,
                DEVELOPER,
                SENIOR,
                2000),
            List.of(
                new ProjectAllocationDisplayDto(
                    1L,
                    1L,
                    1L,
                    LocalDate.now().minusMonths(11),
                    LocalDate.now().plusMonths(11),
                    null,
                    164,
                    SCRUM_MASTER,
                    JUNIOR,
                    2000),
                new ProjectAllocationDisplayDto(
                    1L,
                    2L,
                    1L,
                    LocalDate.now().minusMonths(11),
                    LocalDate.now().plusMonths(11),
                    null,
                    60,
                    SCRUM_MASTER,
                    JUNIOR,
                    2000),
                new ProjectAllocationDisplayDto(
                    1L,
                    3L,
                    1L,
                    LocalDate.now().minusMonths(11),
                    LocalDate.now().plusMonths(11),
                    20,
                    null,
                    TESTER,
                    JUNIOR,
                    2000),
                new ProjectAllocationDisplayDto(
                    1L,
                    4L,
                    1L,
                    LocalDate.now().minusMonths(11),
                    LocalDate.now().plusMonths(11),
                    10,
                    null,
                    TESTER,
                    JUNIOR,
                    2000)),
            new EmployeeDisplayDto(
                1L,
                "qqq",
                "ooo",
                "12345678901",
                LocalDate.now(),
                EMPLOYED,
                Map.of(DEVELOPER, JUNIOR, TESTER, PROFESSIONAL, SCRUM_MASTER, SENIOR)),
            new ProjectDisplayDto(
                1L,
                "xxx",
                "ddd",
                LocalDate.now().minusMonths(11),
                LocalDate.now().plusMonths(11),
                COMPLETED,
                LocalDate.now(),
                LocalDate.now(),
                1L),
            new EnumMap<>(
                Map.of(
                    INVALID_TIME_FRAME,
                    true,
                    INVALID_WORKLOADS_NUMBER,
                    true,
                    INVALID_ROLE,
                    true,
                    INVALID_PROJECT_STATUS,
                    true,
                    INVALID_ALLOCATED_PROJECTS_NUMBER,
                    true,
                    INVALID_DEVELOPER_ROLES_NUMBER,
                    true,
                    INVALID_MAX_WORKLOAD,
                    true))),
        Arguments.of(
            new ProjectAllocationSaveDto(
                1L,
                1L,
                LocalDate.now().minusMonths(11),
                LocalDate.now().plusMonths(12),
                60,
                null,
                TESTER,
                SENIOR,
                2000),
            List.of(
                new ProjectAllocationDisplayDto(
                    1L,
                    1L,
                    1L,
                    LocalDate.now().minusMonths(11),
                    LocalDate.now().plusMonths(11),
                    null,
                    164,
                    SCRUM_MASTER,
                    JUNIOR,
                    2000),
                new ProjectAllocationDisplayDto(
                    1L,
                    2L,
                    1L,
                    LocalDate.now().minusMonths(11),
                    LocalDate.now().plusMonths(11),
                    null,
                    60,
                    SCRUM_MASTER,
                    JUNIOR,
                    2000),
                new ProjectAllocationDisplayDto(
                    1L,
                    3L,
                    1L,
                    LocalDate.now().minusMonths(11),
                    LocalDate.now().plusMonths(11),
                    20,
                    null,
                    TESTER,
                    JUNIOR,
                    2000),
                new ProjectAllocationDisplayDto(
                    1L,
                    4L,
                    1L,
                    LocalDate.now().minusMonths(11),
                    LocalDate.now().plusMonths(11),
                    10,
                    null,
                    TESTER,
                    JUNIOR,
                    2000)),
            new EmployeeDisplayDto(
                1L,
                "qqq",
                "ooo",
                "12345678901",
                LocalDate.now(),
                EMPLOYED,
                Map.of(DEVELOPER, PROFESSIONAL, SCRUM_MASTER, SENIOR)),
            new ProjectDisplayDto(
                1L,
                "xxx",
                "ddd",
                LocalDate.now().minusMonths(11),
                LocalDate.now().plusMonths(11),
                COMPLETED,
                LocalDate.now(),
                LocalDate.now(),
                1L),
            new EnumMap<>(
                Map.of(
                    INVALID_TIME_FRAME,
                    true,
                    INVALID_WORKLOADS_NUMBER,
                    false,
                    INVALID_ROLE,
                    true,
                    INVALID_PROJECT_STATUS,
                    true,
                    INVALID_ALLOCATED_PROJECTS_NUMBER,
                    true,
                    INVALID_DEVELOPER_ROLES_NUMBER,
                    false,
                    INVALID_MAX_WORKLOAD,
                    true))));
  }

  static Stream<Arguments> invalidProjectAllocationsForUpdate() {
    return Stream.of(
        Arguments.of(
            new ProjectAllocationSaveDto(
                1L,
                1L,
                LocalDate.now().minusMonths(12),
                LocalDate.now().plusMonths(12),
                40,
                20,
                TESTER,
                SENIOR,
                2000),
            List.of(
                new ProjectAllocationDisplayDto(
                    1L,
                    1L,
                    1L,
                    LocalDate.now().minusMonths(11),
                    LocalDate.now().plusMonths(11),
                    null,
                    164,
                    SCRUM_MASTER,
                    JUNIOR,
                    2000),
                new ProjectAllocationDisplayDto(
                    2L,
                    2L,
                    1L,
                    LocalDate.now().minusMonths(11),
                    LocalDate.now().plusMonths(11),
                    null,
                    60,
                    SCRUM_MASTER,
                    JUNIOR,
                    2000),
                new ProjectAllocationDisplayDto(
                    3L,
                    3L,
                    1L,
                    LocalDate.now().minusMonths(11),
                    LocalDate.now().plusMonths(11),
                    20,
                    null,
                    TESTER,
                    JUNIOR,
                    2000),
                new ProjectAllocationDisplayDto(
                    4L,
                    4L,
                    1L,
                    LocalDate.now().minusMonths(11),
                    LocalDate.now().plusMonths(11),
                    10,
                    null,
                    TESTER,
                    JUNIOR,
                    2000),
                new ProjectAllocationDisplayDto(
                    5L,
                    4L,
                    1L,
                    LocalDate.now().minusMonths(11),
                    LocalDate.now().plusMonths(11),
                    10,
                    null,
                    TESTER,
                    JUNIOR,
                    2000)),
            new EmployeeDisplayDto(
                1L,
                "qqq",
                "ooo",
                "12345678901",
                LocalDate.now(),
                EMPLOYED,
                Map.of(TESTER, PROFESSIONAL, SCRUM_MASTER, SENIOR)),
            new ProjectDisplayDto(
                1L,
                "xxx",
                "ddd",
                LocalDate.now().minusMonths(11),
                LocalDate.now().plusMonths(11),
                COMPLETED,
                LocalDate.now(),
                LocalDate.now(),
                1L),
            new EnumMap<>(
                Map.of(
                    INVALID_TIME_FRAME,
                    true,
                    INVALID_WORKLOADS_NUMBER,
                    true,
                    INVALID_ROLE,
                    true,
                    INVALID_PROJECT_STATUS,
                    true,
                    INVALID_ALLOCATED_PROJECTS_NUMBER,
                    true,
                    INVALID_DEVELOPER_ROLES_NUMBER,
                    false,
                    INVALID_MAX_WORKLOAD,
                    true))),
        Arguments.of(
            new ProjectAllocationSaveDto(
                1L,
                1L,
                LocalDate.now().minusMonths(12),
                LocalDate.now().plusMonths(12),
                40,
                20,
                DEVELOPER,
                SENIOR,
                2000),
            List.of(
                new ProjectAllocationDisplayDto(
                    1L,
                    1L,
                    1L,
                    LocalDate.now().minusMonths(11),
                    LocalDate.now().plusMonths(11),
                    null,
                    164,
                    SCRUM_MASTER,
                    JUNIOR,
                    2000),
                new ProjectAllocationDisplayDto(
                    2L,
                    2L,
                    1L,
                    LocalDate.now().minusMonths(11),
                    LocalDate.now().plusMonths(11),
                    null,
                    60,
                    SCRUM_MASTER,
                    JUNIOR,
                    2000),
                new ProjectAllocationDisplayDto(
                    3L,
                    3L,
                    1L,
                    LocalDate.now().minusMonths(11),
                    LocalDate.now().plusMonths(11),
                    20,
                    null,
                    TESTER,
                    JUNIOR,
                    2000),
                new ProjectAllocationDisplayDto(
                    4L,
                    4L,
                    1L,
                    LocalDate.now().minusMonths(11),
                    LocalDate.now().plusMonths(11),
                    10,
                    null,
                    TESTER,
                    JUNIOR,
                    2000),
                new ProjectAllocationDisplayDto(
                    5L,
                    4L,
                    1L,
                    LocalDate.now().minusMonths(11),
                    LocalDate.now().plusMonths(11),
                    10,
                    null,
                    TESTER,
                    JUNIOR,
                    2000)),
            new EmployeeDisplayDto(
                1L,
                "qqq",
                "ooo",
                "12345678901",
                LocalDate.now(),
                EMPLOYED,
                Map.of(DEVELOPER, JUNIOR, TESTER, PROFESSIONAL, SCRUM_MASTER, SENIOR)),
            new ProjectDisplayDto(
                1L,
                "xxx",
                "ddd",
                LocalDate.now().minusMonths(11),
                LocalDate.now().plusMonths(11),
                COMPLETED,
                LocalDate.now(),
                LocalDate.now(),
                1L),
            new EnumMap<>(
                Map.of(
                    INVALID_TIME_FRAME,
                    true,
                    INVALID_WORKLOADS_NUMBER,
                    true,
                    INVALID_ROLE,
                    true,
                    INVALID_PROJECT_STATUS,
                    true,
                    INVALID_ALLOCATED_PROJECTS_NUMBER,
                    true,
                    INVALID_DEVELOPER_ROLES_NUMBER,
                    true,
                    INVALID_MAX_WORKLOAD,
                    true))),
        Arguments.of(
            new ProjectAllocationSaveDto(
                1L,
                1L,
                LocalDate.now().minusMonths(11),
                LocalDate.now().plusMonths(12),
                60,
                null,
                TESTER,
                SENIOR,
                2000),
            List.of(
                new ProjectAllocationDisplayDto(
                    1L,
                    1L,
                    1L,
                    LocalDate.now().minusMonths(11),
                    LocalDate.now().plusMonths(11),
                    null,
                    164,
                    SCRUM_MASTER,
                    JUNIOR,
                    2000),
                new ProjectAllocationDisplayDto(
                    2L,
                    2L,
                    1L,
                    LocalDate.now().minusMonths(11),
                    LocalDate.now().plusMonths(11),
                    null,
                    60,
                    SCRUM_MASTER,
                    JUNIOR,
                    2000),
                new ProjectAllocationDisplayDto(
                    3L,
                    3L,
                    1L,
                    LocalDate.now().minusMonths(11),
                    LocalDate.now().plusMonths(11),
                    20,
                    null,
                    TESTER,
                    JUNIOR,
                    2000),
                new ProjectAllocationDisplayDto(
                    4L,
                    4L,
                    1L,
                    LocalDate.now().minusMonths(11),
                    LocalDate.now().plusMonths(11),
                    10,
                    null,
                    TESTER,
                    JUNIOR,
                    2000),
                new ProjectAllocationDisplayDto(
                    5L,
                    4L,
                    1L,
                    LocalDate.now().minusMonths(11),
                    LocalDate.now().plusMonths(11),
                    10,
                    null,
                    TESTER,
                    JUNIOR,
                    2000)),
            new EmployeeDisplayDto(
                1L,
                "qqq",
                "ooo",
                "12345678901",
                LocalDate.now(),
                EMPLOYED,
                Map.of(DEVELOPER, PROFESSIONAL, SCRUM_MASTER, SENIOR)),
            new ProjectDisplayDto(
                1L,
                "xxx",
                "ddd",
                LocalDate.now().minusMonths(11),
                LocalDate.now().plusMonths(11),
                COMPLETED,
                LocalDate.now(),
                LocalDate.now(),
                1L),
            new EnumMap<>(
                Map.of(
                    INVALID_TIME_FRAME,
                    true,
                    INVALID_WORKLOADS_NUMBER,
                    false,
                    INVALID_ROLE,
                    true,
                    INVALID_PROJECT_STATUS,
                    true,
                    INVALID_ALLOCATED_PROJECTS_NUMBER,
                    true,
                    INVALID_DEVELOPER_ROLES_NUMBER,
                    false,
                    INVALID_MAX_WORKLOAD,
                    true))));
  }
}
