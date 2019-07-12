package com.wojciechdm.programmers.company.structure.projectallocation;

import static java.util.Objects.*;

import com.wojciechdm.programmers.company.structure.employee.EmployeeDisplayDto;
import com.wojciechdm.programmers.company.structure.employee.EmployeeRole;
import com.wojciechdm.programmers.company.structure.project.ProjectDisplayDto;
import com.wojciechdm.programmers.company.structure.project.ProjectStatus;

import java.math.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.*;

class ProjectAllocationValidator {

  ProjectAllocationValidationResult validate(
      ProjectAllocationSaveDto projectAllocation,
      List<ProjectAllocationDisplayDto> projectAllocations,
      EmployeeDisplayDto employee,
      ProjectDisplayDto project) {

    return ProjectAllocationValidationResult.getInstance(
        prepareRulesViolations(projectAllocation, projectAllocations, employee, project));
  }

  ProjectAllocationValidationResult validate(
      long id,
      ProjectAllocationSaveDto projectAllocation,
      List<ProjectAllocationDisplayDto> projectAllocations,
      EmployeeDisplayDto employee,
      ProjectDisplayDto project) {

    return ProjectAllocationValidationResult.getInstance(
        prepareRulesViolations(
            projectAllocation,
            findNotUpdatingProjectAllocations(id, projectAllocations),
            employee,
            project));
  }

  private EnumMap<ProjectAllocationRuleViolation, Boolean> prepareRulesViolations(
      ProjectAllocationSaveDto projectAllocation,
      List<ProjectAllocationDisplayDto> projectAllocations,
      EmployeeDisplayDto employee,
      ProjectDisplayDto project) {

    EnumMap<ProjectAllocationRuleViolation, Boolean> rulesViolations =
        new EnumMap<>(ProjectAllocationRuleViolation.class);
    rulesViolations.put(ProjectAllocationRuleViolation.INVALID_TIME_FRAME, hasInvalidTimeFrame(projectAllocation, project));
    rulesViolations.put(ProjectAllocationRuleViolation.INVALID_WORKLOADS_NUMBER, hasMoreThanOneWorkloadKind(projectAllocation));
    rulesViolations.put(ProjectAllocationRuleViolation.INVALID_ROLE, hasInvalidRoleOrLevel(projectAllocation, employee));
    rulesViolations.put(ProjectAllocationRuleViolation.INVALID_PROJECT_STATUS, hasUnavailableStatus(project));
    rulesViolations.put(
        ProjectAllocationRuleViolation.INVALID_ALLOCATED_PROJECTS_NUMBER,
        hasInvalidAllocatedProjectsNumber(projectAllocations, projectAllocation));
    rulesViolations.put(
        ProjectAllocationRuleViolation.INVALID_DEVELOPER_ROLES_NUMBER,
        isMoreThanOneRoleInProjectForDeveloper(projectAllocations, projectAllocation));
    rulesViolations.put(
        ProjectAllocationRuleViolation.INVALID_MAX_WORKLOAD, hasInvalidMaxWorkload(projectAllocations, projectAllocation));

    return rulesViolations;
  }

  private List<ProjectAllocationDisplayDto> findNotUpdatingProjectAllocations(
      long id, List<ProjectAllocationDisplayDto> projectAllocations) {

    return projectAllocations.stream()
        .filter(projectAllocation -> projectAllocation.getId() != id)
        .collect(Collectors.toList());
  }

  private boolean hasInvalidTimeFrame(
      ProjectAllocationSaveDto projectAllocation, ProjectDisplayDto project) {

    return (projectAllocation.getStartDate().isBefore(project.getStartDate()))
        || (!isNull(projectAllocation.getEndDate())
            && !isNull(project.getEndDate())
            && projectAllocation.getEndDate().isAfter(project.getEndDate()));
  }

  private boolean hasMoreThanOneWorkloadKind(ProjectAllocationSaveDto projectAllocation) {

    return (!nonNull(projectAllocation.getPercentileWorkload())
            || !isNull(projectAllocation.getHoursPerMonthWorkload()))
        && (!isNull(projectAllocation.getPercentileWorkload())
            || !nonNull(projectAllocation.getHoursPerMonthWorkload()));
  }

  private boolean hasInvalidRoleOrLevel(
      ProjectAllocationSaveDto projectAllocation, EmployeeDisplayDto employee) {

    return !employee.getRoles().containsKey(projectAllocation.getRole())
        || projectAllocation.getLevel().getNumericLevel()
            > employee.getRoles().get(projectAllocation.getRole()).getNumericLevel();
  }

  private boolean hasUnavailableStatus(ProjectDisplayDto project) {

    return project.getStatus() == ProjectStatus.SALE || project.getStatus() == ProjectStatus.COMPLETED;
  }

  private boolean hasInvalidAllocatedProjectsNumber(
      List<ProjectAllocationDisplayDto> projectAllocations,
      ProjectAllocationSaveDto projectAllocation) {

    Stream<ProjectAllocationDisplayDto> activeAllocations =
        findActiveAllocations(projectAllocations);

    return projectAllocation.getRole() == EmployeeRole.DEVELOPER
        ? activeAllocations.count() > 1
        : projectAllocation.getRole() == EmployeeRole.TESTER
            && activeAllocations
                    .map(ProjectAllocationDisplayDto::getProjectId)
                    .distinct()
                    .filter(projectId -> !projectId.equals(projectAllocation.getProjectId()))
                    .count()
                > 2;
  }

  private boolean isMoreThanOneRoleInProjectForDeveloper(
      List<ProjectAllocationDisplayDto> projectAllocations,
      ProjectAllocationSaveDto projectAllocation) {

    return (projectAllocation.getRole() == EmployeeRole.DEVELOPER)
        && findActiveAllocationsForProject(projectAllocations, projectAllocation).count() != 0;
  }

  private boolean hasInvalidMaxWorkload(
      List<ProjectAllocationDisplayDto> projectAllocations,
      ProjectAllocationSaveDto projectAllocation) {

    int averageWorkHoursPerMonth = 164;
    int maxPercentWorkload = 120;
    int oneHundredPercent = 100;

    return new BigDecimal(maxPercentWorkload)
            .compareTo(
                new BigDecimal(
                        calculateHourlyWorkloadSum(projectAllocations, projectAllocation)
                            * oneHundredPercent)
                    .divide(new BigDecimal(averageWorkHoursPerMonth), 1, RoundingMode.HALF_UP)
                    .add(
                        new BigDecimal(
                            calculatePercentileWorkloadSum(projectAllocations, projectAllocation))))
        < 0;
  }

  private Stream<ProjectAllocationDisplayDto> findActiveAllocations(
      List<ProjectAllocationDisplayDto> projectAllocations) {

    return projectAllocations.stream().filter(this::isActive);
  }

  private Stream<ProjectAllocationDisplayDto> findActiveAllocationsForProject(
      List<ProjectAllocationDisplayDto> projectAllocations,
      ProjectAllocationSaveDto projectAllocation) {

    return findActiveAllocations(projectAllocations)
        .filter(allocation -> allocation.getProjectId().equals(projectAllocation.getProjectId()));
  }

  private int calculatePercentileWorkloadSum(
      List<ProjectAllocationDisplayDto> projectAllocations,
      ProjectAllocationSaveDto projectAllocation) {

    return findActiveAllocationsForProject(projectAllocations, projectAllocation)
            .filter(allocation -> Objects.nonNull(allocation.getPercentileWorkload()))
            .mapToInt(ProjectAllocationDisplayDto::getPercentileWorkload)
            .sum()
        + Optional.of(projectAllocation)
            .map(ProjectAllocationSaveDto::getPercentileWorkload)
            .orElse(0);
  }

  private int calculateHourlyWorkloadSum(
      List<ProjectAllocationDisplayDto> projectAllocations,
      ProjectAllocationSaveDto projectAllocation) {

    return findActiveAllocationsForProject(projectAllocations, projectAllocation)
            .filter(allocation -> Objects.nonNull(allocation.getHoursPerMonthWorkload()))
            .mapToInt(ProjectAllocationDisplayDto::getHoursPerMonthWorkload)
            .sum()
        + Optional.of(projectAllocation)
            .map(ProjectAllocationSaveDto::getHoursPerMonthWorkload)
            .orElse(0);
  }

  private boolean isActive(ProjectAllocationDisplayDto projectAllocation) {
    return isNull(projectAllocation.getEndDate())
        || !LocalDate.now().isAfter(projectAllocation.getEndDate());
  }
}
