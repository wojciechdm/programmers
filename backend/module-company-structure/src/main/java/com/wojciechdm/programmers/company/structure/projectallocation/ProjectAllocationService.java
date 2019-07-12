package com.wojciechdm.programmers.company.structure.projectallocation;

import com.wojciechdm.programmers.company.structure.employee.EmployeeServiceFacade;
import com.wojciechdm.programmers.company.structure.project.ProjectServiceFacade;
import lombok.AllArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
class ProjectAllocationService {

  ProjectAllocationDao projectAllocationDao;
  ProjectAllocationMapper projectAllocationMapper;
  ProjectAllocationValidator projectAllocationValidator;
  EmployeeServiceFacade employeeService;
  ProjectServiceFacade projectService;

  ProjectAllocationDisplayDto save(ProjectAllocationSaveDto projectAllocationSaveDto) {

    ProjectAllocationValidationResult projectAllocationValidationResult =
        projectAllocationValidator.validate(
            projectAllocationSaveDto,
            fetchByEmployeeId(projectAllocationSaveDto.getEmployeeId()),
            employeeService.fetch(projectAllocationSaveDto.getEmployeeId()),
            projectService.fetch(projectAllocationSaveDto.getProjectId()));

    Optional.of(projectAllocationValidationResult)
        .filter(ProjectAllocationValidationResult::isValid)
        .orElseThrow(
            () ->
                new ProjectAllocationValidationException(
                    projectAllocationValidationResult.extractOccurredRulesViolationsAsString()));

    return projectAllocationMapper.toDisplayDto(
        projectAllocationDao.save(projectAllocationMapper.toEntity(projectAllocationSaveDto)));
  }

  ProjectAllocationDisplayDto update(long id, ProjectAllocationSaveDto projectAllocationSaveDto) {

    Optional.of(id)
        .filter(projectAllocationDao::exists)
        .orElseThrow(
            () ->
                new ProjectAllocationNotFoundException(
                    "Project allocation id: " + id + " not found"));

    ProjectAllocationValidationResult projectAllocationValidationResult =
        projectAllocationValidator.validate(
            id,
            projectAllocationSaveDto,
            fetchByEmployeeId(projectAllocationSaveDto.getEmployeeId()),
            employeeService.fetch(projectAllocationSaveDto.getEmployeeId()),
            projectService.fetch(projectAllocationSaveDto.getProjectId()));

    Optional.of(projectAllocationValidationResult)
        .filter(ProjectAllocationValidationResult::isValid)
        .orElseThrow(
            () ->
                new ProjectAllocationValidationException(
                    projectAllocationValidationResult.extractOccurredRulesViolationsAsString()));

    return projectAllocationMapper.toDisplayDto(
        projectAllocationDao.update(
            projectAllocationMapper.toEntity(id, projectAllocationSaveDto)));
  }

  boolean delete(long id) {

    return projectAllocationDao.delete(id);
  }

  ProjectAllocationDisplayDto fetch(long id) {

    return projectAllocationDao
        .fetch(id)
        .map(projectAllocationMapper::toDisplayDto)
        .orElseThrow(
            () ->
                new ProjectAllocationNotFoundException(
                    "Project allocation id: " + id + " not found"));
  }

  List<ProjectAllocationDisplayDto> fetchByEmployeeId(long id) {

    return projectAllocationDao.fetchByEmployeeId(id).stream()
        .map(projectAllocationMapper::toDisplayDto)
        .collect(Collectors.toList());
  }

  List<ProjectAllocationDisplayDto> fetchByProjectId(long id) {

    return projectAllocationDao.fetchByProjectId(id).stream()
        .map(projectAllocationMapper::toDisplayDto)
        .collect(Collectors.toList());
  }

  ProjectAllocationFetchAllResponse fetchAll(
      int page, int limit, ProjectAllocationSortProperty sortProperty, boolean sortDesc) {
    return new ProjectAllocationFetchAllResponse(
        projectAllocationDao.count(),
        projectAllocationDao.fetchAll(page, limit, sortProperty, sortDesc).stream()
            .map(projectAllocationMapper::toDisplayDto)
            .collect(Collectors.toList()));
  }
}
