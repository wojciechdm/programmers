package com.wojciechdm.programmers.company.structure.project;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.*;

import java.util.List;

public class ProjectServiceFacade {

  private ProjectService projectService;

  public ProjectServiceFacade(ProjectDao projectDao) {
    projectService = new ProjectService(projectDao, new ProjectMapper());
  }

  public ProjectDisplayDto save(ProjectSaveDto projectSaveDto) {

    checkArgument(nonNull(projectSaveDto), "Project can not be null");

    return projectService.save(projectSaveDto);
  }

  public ProjectDisplayDto update(long id, ProjectSaveDto projectSaveDto) {

    checkArgument(nonNull(projectSaveDto), "Project can not be null");

    return projectService.update(id, projectSaveDto);
  }

  public boolean delete(long id) {

    return projectService.delete(id);
  }

  public ProjectDisplayDto fetch(long id) {

    return projectService.fetch(id);
  }

  public List<ProjectDisplayDto> fetchByClientId(long id) {

    return projectService.fetchByClientId(id);
  }

  public ProjectFetchAllResponse fetchAll(
      int page, int limit, ProjectSortProperty sortProperty, boolean sortDesc) {

    return projectService.fetchAll(page, limit, sortProperty, sortDesc);
  }
}
