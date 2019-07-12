package com.wojciechdm.programmers.company.structure.project;

import java.time.LocalDate;

class ProjectMapper {

  ProjectDisplayDto toDisplayDto(Project project) {
    return new ProjectDisplayDto(
        project.getId(),
        project.getName(),
        project.getCodeName(),
        project.getStartDate(),
        project.getEndDate(),
        project.getStatus(),
        project.getCreateDate(),
        project.getLastModificationDate(),
        project.getClientId());
  }

  Project toEntity(long id, ProjectSaveDto projectSaveDto) {
    return new Project(
        id,
        projectSaveDto.getName(),
        projectSaveDto.getCodeName(),
        projectSaveDto.getStartDate(),
        projectSaveDto.getEndDate(),
        projectSaveDto.getStatus(),
        null,
        LocalDate.now(),
        projectSaveDto.getClientId());
  }

  Project toEntity(ProjectSaveDto projectSaveDto) {
    return new Project(
        null,
        projectSaveDto.getName(),
        projectSaveDto.getCodeName(),
        projectSaveDto.getStartDate(),
        projectSaveDto.getEndDate(),
        projectSaveDto.getStatus(),
        LocalDate.now(),
        LocalDate.now(),
        projectSaveDto.getClientId());
  }
}
