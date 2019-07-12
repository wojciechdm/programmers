package com.wojciechdm.programmers.controllers.rest.project;

import com.wojciechdm.programmers.company.structure.project.*;
import com.wojciechdm.programmers.company.structure.projectallocation.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/projects")
@Slf4j
public class ProjectController {

  private final ProjectServiceFacade projectServiceFacade;
  private final ProjectAllocationServiceFacade projectAllocationServiceFacade;

  @ResponseStatus(CREATED)
  @PostMapping
  public ProjectDisplayDto save(@RequestBody ProjectSaveDto projectSaveDto) {

    ProjectDisplayDto projectSaved = projectServiceFacade.save(projectSaveDto);
    log.info(
        "Project id: "
            + projectSaved.getId()
            + ", name: "
            + projectSaved.getName()
            + ", codeName: "
            + projectSaved.getCodeName()
            + " saved");

    return projectSaved;
  }

  @PutMapping(path = "/{id}")
  public ProjectDisplayDto update(
      @PathVariable("id") long id, @RequestBody ProjectSaveDto projectSaveDto) {

    ProjectDisplayDto projectUpdated = projectServiceFacade.update(id, projectSaveDto);
    log.info(
        "Project id: "
            + projectUpdated.getId()
            + ", name: "
            + projectUpdated.getName()
            + ", codeName: "
            + projectUpdated.getCodeName()
            + " updated");

    return projectUpdated;
  }

  @DeleteMapping(path = "/{id}")
  public boolean delete(@PathVariable("id") long id) {

    boolean projectDeleted = projectServiceFacade.delete(id);
    log.info("Project id:" + id + " deleted");

    return projectDeleted;
  }

  @GetMapping(path = "/{id}")
  public ProjectDisplayDto fetch(@PathVariable("id") long id) {

    return projectServiceFacade.fetch(id);
  }

  @GetMapping(path = "/{id}/projectallocations")
  public List<ProjectAllocationDisplayDto> fetchProjectAllocations(@PathVariable("id") long id) {

    return projectAllocationServiceFacade.fetchByProjectId(id);
  }

  @GetMapping
  public ProjectFetchAllResponse fetchAll(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "20") int limit,
      @RequestParam(defaultValue = "ID") ProjectSortProperty sortProperty,
      @RequestParam(defaultValue = "false") boolean sortDesc) {

    return projectServiceFacade.fetchAll(page, limit, sortProperty, sortDesc);
  }
}
