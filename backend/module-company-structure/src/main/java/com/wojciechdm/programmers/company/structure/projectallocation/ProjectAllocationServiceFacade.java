package com.wojciechdm.programmers.company.structure.projectallocation;

import com.wojciechdm.programmers.company.structure.employee.EmployeeServiceFacade;
import com.wojciechdm.programmers.company.structure.project.ProjectServiceFacade;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.*;

import java.util.List;

public class ProjectAllocationServiceFacade {

  private ProjectAllocationService projectAllocationService;

  public ProjectAllocationServiceFacade(
      ProjectAllocationDao projectAllocationDao,
      EmployeeServiceFacade employeeService,
      ProjectServiceFacade projectService) {
    projectAllocationService =
        new ProjectAllocationService(
            projectAllocationDao,
            new ProjectAllocationMapper(),
            new ProjectAllocationValidator(),
            employeeService,
            projectService);
  }

  public ProjectAllocationDisplayDto save(ProjectAllocationSaveDto projectAllocationSaveDto) {

    checkArgument(nonNull(projectAllocationSaveDto), "ProjectAllocation can not be null");

    return projectAllocationService.save(projectAllocationSaveDto);
  }

  public ProjectAllocationDisplayDto update(
      long id, ProjectAllocationSaveDto projectAllocationSaveDto) {

    checkArgument(nonNull(projectAllocationSaveDto), "ProjectAllocation can not be null");

    return projectAllocationService.update(id, projectAllocationSaveDto);
  }

  public boolean delete(long id) {

    return projectAllocationService.delete(id);
  }

  public ProjectAllocationDisplayDto fetch(long id) {

    return projectAllocationService.fetch(id);
  }

  public List<ProjectAllocationDisplayDto> fetchByEmployeeId(long id) {

    return projectAllocationService.fetchByEmployeeId(id);
  }

  public List<ProjectAllocationDisplayDto> fetchByProjectId(long id) {

    return projectAllocationService.fetchByProjectId(id);
  }

  public ProjectAllocationFetchAllResponse fetchAll(
      int page, int limit, ProjectAllocationSortProperty sortProperty, boolean sortDesc) {

    return projectAllocationService.fetchAll(page, limit, sortProperty, sortDesc);
  }
}
