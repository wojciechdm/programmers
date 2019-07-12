package com.wojciechdm.programmers.company.structure.projectallocation

import com.wojciechdm.programmers.company.structure.project.ProjectDisplayDto
import com.wojciechdm.programmers.company.structure.projectallocation.ProjectAllocationDisplayDto
import com.wojciechdm.programmers.company.structure.projectallocation.ProjectAllocationSaveDto
import spock.lang.Unroll

import static com.wojciechdm.programmers.company.structure.employee.EmployeeLevel.*
import static com.wojciechdm.programmers.company.structure.employee.EmployeeRole.*
import static com.wojciechdm.programmers.company.structure.employee.EmployeeStatus.*
import static com.wojciechdm.programmers.company.structure.project.ProjectStatus.*
import static ProjectAllocationRuleViolation.*

import com.wojciechdm.programmers.company.structure.employee.EmployeeDisplayDto
import spock.lang.Specification

import java.time.LocalDate

class ProjectAllocationValidatorSpockTest extends Specification {

    private def projectAllocationValidator = new ProjectAllocationValidator()

    @Unroll
    def "should validate projectAllocation for save and say it is valid"() {

        when:

        def actual = projectAllocationValidator.validate(projectAllocation, projectAllocations, employee, project)

        then:

        actual.isValid()

        where:

        projectAllocation << validProjectsAllocation()
        projectAllocations << projectAllocationsForValidAllocationToSave()
        employee << employeeForValidAllocation()
        project << projectForValidAllocation()
    }

    @Unroll("should validate projectAllocation for save and return violations rules: #expectedRulesViolations")
    def "should validate projectAllocation for save and return occurred rules violations"() {

        when:

        def actual = projectAllocationValidator.validate(projectAllocation, projectAllocations, employee, project)

        then:

        actual.getRulesViolations() == expectedRulesViolations

        where:

        projectAllocation << invalidProjectAllocation()
        projectAllocations << projectAllocationsForInvalidAllocationToSave()
        employee << employeeForInvalidAllocation()
        project << projectForInvalidAllocation()
        expectedRulesViolations << rulesViolations()
    }


    @Unroll
    def "should validate projectAllocation for update and say it is valid"() {

        when:

        def actual = projectAllocationValidator.validate(4L, projectAllocation, projectAllocations, employee, project)

        then:

        actual.isValid()

        where:

        projectAllocation << validProjectsAllocation()
        projectAllocations << projectAllocationsForValidAllocationToUpdate()
        employee << employeeForValidAllocation()
        project << projectForValidAllocation()
    }

    @Unroll("should validate projectAllocation for update and return violations rules: #expectedRulesViolations")
    def "should validate projectAllocation for update and return occurred rules violations"() {

        when:

        def actual = projectAllocationValidator.validate(5L, projectAllocation, projectAllocations, employee, project)

        then:

        actual.getRulesViolations() == expectedRulesViolations

        where:

        projectAllocation << invalidProjectAllocation()
        projectAllocations << projectAllocationsForInvalidAllocationToUpdate()
        employee << employeeForInvalidAllocation()
        project << projectForInvalidAllocation()
        expectedRulesViolations << rulesViolations()
    }

    def validProjectsAllocation() {
        [new ProjectAllocationSaveDto(
                1L,
                1L,
                LocalDate.now().minusMonths(10),
                LocalDate.now().plusMonths(10),
                40,
                null,
                TESTER,
                JUNIOR,
                2000)]
    }

    def projectAllocationsForValidAllocationToSave() {
        [[new ProjectAllocationDisplayDto(
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
                  2000),]]
    }

    def projectAllocationsForValidAllocationToUpdate() {
        [[new ProjectAllocationDisplayDto(
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
                  2000)
         ]]
    }

    def employeeForValidAllocation() {
        [new EmployeeDisplayDto(
                1L,
                "qqq",
                "ooo",
                "12345678901",
                LocalDate.now(),
                EMPLOYED,
                Map.of(TESTER, PROFESSIONAL, SCRUM_MASTER, SENIOR, DEVELOPER, SENIOR))]
    }

    def projectForValidAllocation() {
        [new ProjectDisplayDto(
                1L,
                "xxx",
                "ddd",
                LocalDate.now().minusMonths(10),
                LocalDate.now().plusMonths(11),
                ACTIVE,
                LocalDate.now(),
                LocalDate.now(),
                1L)]
    }

    def invalidProjectAllocation() {
        [new ProjectAllocationSaveDto(
                1L,
                1L,
                LocalDate.now().minusMonths(12),
                LocalDate.now().plusMonths(12),
                40,
                20,
                TESTER,
                SENIOR,
                2000),
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
         new ProjectAllocationSaveDto(
                 1L,
                 1L,
                 LocalDate.now().minusMonths(11),
                 LocalDate.now().plusMonths(12),
                 60,
                 null,
                 TESTER,
                 SENIOR,
                 2000)]
    }

    def projectAllocationsForInvalidAllocationToSave() {
        [[new ProjectAllocationDisplayDto(
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
                  2000)],
         [new ProjectAllocationDisplayDto(
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
                  2000)],
         [new ProjectAllocationDisplayDto(
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
                  2000)]]
    }

    def projectAllocationsForInvalidAllocationToUpdate() {
        [[new ProjectAllocationDisplayDto(
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
                  2000)],
         [new ProjectAllocationDisplayDto(
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
                  2000)],
         [new ProjectAllocationDisplayDto(
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
                  2000)]]
    }

    def employeeForInvalidAllocation() {
        [new EmployeeDisplayDto(
                1L,
                "qqq",
                "ooo",
                "12345678901",
                LocalDate.now(),
                EMPLOYED,
                Map.of(TESTER, PROFESSIONAL, SCRUM_MASTER, SENIOR)),
         new EmployeeDisplayDto(
                 1L,
                 "qqq",
                 "ooo",
                 "12345678901",
                 LocalDate.now(),
                 EMPLOYED,
                 Map.of(DEVELOPER, JUNIOR, TESTER, PROFESSIONAL, SCRUM_MASTER, SENIOR)),
         new EmployeeDisplayDto(
                 1L,
                 "qqq",
                 "ooo",
                 "12345678901",
                 LocalDate.now(),
                 EMPLOYED,
                 Map.of(DEVELOPER, PROFESSIONAL, SCRUM_MASTER, SENIOR))]
    }

    def projectForInvalidAllocation() {
        [new ProjectDisplayDto(
                1L,
                "xxx",
                "ddd",
                LocalDate.now().minusMonths(11),
                LocalDate.now().plusMonths(11),
                COMPLETED,
                LocalDate.now(),
                LocalDate.now(),
                1L),
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
         new ProjectDisplayDto(
                 1L,
                 "xxx",
                 "ddd",
                 LocalDate.now().minusMonths(11),
                 LocalDate.now().plusMonths(11),
                 COMPLETED,
                 LocalDate.now(),
                 LocalDate.now(),
                 1L)]
    }

    def rulesViolations() {
        [new EnumMap<>(
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
                        true)),
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
                         true)),
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
                         true))]
    }
}
