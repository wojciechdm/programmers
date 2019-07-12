package com.wojciechdm.programmers.company.structure.project;

import lombok.AllArgsConstructor;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ProjectService {

  private ProjectDao projectDao;
  private ProjectMapper projectMapper;

  ProjectDisplayDto save(ProjectSaveDto projectSaveDto) {
    return projectMapper.toDisplayDto(projectDao.save(projectMapper.toEntity(projectSaveDto)));
  }

  ProjectDisplayDto update(long id, ProjectSaveDto projectSaveDto) {
    return Optional.of(projectMapper.toEntity(id, projectSaveDto))
        .filter(project -> projectDao.exists(project.getId()))
        .map(projectDao::update)
        .map(projectMapper::toDisplayDto)
        .orElseThrow(() -> new ProjectNotFoundException("Project id: " + id + " not found"));
  }

  boolean delete(long id) {
    return projectDao.delete(id);
  }

  public ProjectDisplayDto fetch(long id) {
    return projectDao
        .fetch(id)
        .map(projectMapper::toDisplayDto)
        .orElseThrow(() -> new ProjectNotFoundException("Project id: " + id + " not found"));
  }

  List<ProjectDisplayDto> fetchByClientId(long id) {
    return projectDao.fetchByClientId(id).stream()
        .map(projectMapper::toDisplayDto)
        .collect(Collectors.toList());
  }

  ProjectFetchAllResponse fetchAll(
      int page, int limit, ProjectSortProperty sortProperty, boolean sortDesc) {
    return new ProjectFetchAllResponse(
        projectDao.count(),
        projectDao.fetchAll(page, limit, sortProperty, sortDesc).stream()
            .map(projectMapper::toDisplayDto)
            .collect(Collectors.toList()));
  }
}
