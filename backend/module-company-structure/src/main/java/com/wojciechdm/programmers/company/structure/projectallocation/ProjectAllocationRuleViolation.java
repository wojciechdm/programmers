package com.wojciechdm.programmers.company.structure.projectallocation;

import lombok.*;

@Getter
@AllArgsConstructor
enum ProjectAllocationRuleViolation {
  INVALID_TIME_FRAME(
      "Start date of project allocation cannot be before start date of project and "
          + "end date of project allocation cannot be after end date of project"),
  INVALID_WORKLOADS_NUMBER("There cannot be both of percentile or hourly workload"),
  INVALID_ROLE(
      "Cannot allocate an employee to the project, in a role that is not allocated to it and "
          + "a level higher than that currently specified in the given role"),
  INVALID_PROJECT_STATUS("Cannot allocate project with completed or sale status"),
  INVALID_ALLOCATED_PROJECTS_NUMBER(
      "Cannot allocate developer to more than two projects and tester to more than three projects"),
  INVALID_DEVELOPER_ROLES_NUMBER("Cannot allocate developer to more roles for one project"),
  INVALID_MAX_WORKLOAD("Workload for one project cannot be more than 120 percent");

  private String message;
}
