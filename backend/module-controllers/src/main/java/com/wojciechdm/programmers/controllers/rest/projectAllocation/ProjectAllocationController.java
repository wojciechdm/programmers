package com.wojciechdm.programmers.controllers.rest.projectAllocation;

import com.wojciechdm.programmers.company.structure.projectallocation.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/projectallocations")
@Slf4j
public class ProjectAllocationController {

  private final ProjectAllocationServiceFacade projectAllocationServiceFacade;

  @ResponseStatus(CREATED)
  @PostMapping
  public ProjectAllocationDisplayDto save(
      @RequestBody ProjectAllocationSaveDto projectAllocationSaveDto) {

    ProjectAllocationDisplayDto projectAllocationSaved =
        projectAllocationServiceFacade.save(projectAllocationSaveDto);
    log.info(
        "ProjectAllocation id: "
            + projectAllocationSaved.getId()
            + ", projectId: "
            + projectAllocationSaved.getProjectId()
            + ", employeeId: "
            + projectAllocationSaved.getEmployeeId()
            + " saved");

    return projectAllocationSaved;
  }

  @PutMapping(path = "/{id}")
  public ProjectAllocationDisplayDto update(
      @PathVariable("id") long id, @RequestBody ProjectAllocationSaveDto projectAllocationSaveDto) {

    ProjectAllocationDisplayDto projectAllocationUpdated =
        projectAllocationServiceFacade.update(id, projectAllocationSaveDto);
    log.info(
        "ProjectAllocation id: "
            + projectAllocationUpdated.getId()
            + ", projectId: "
            + projectAllocationUpdated.getProjectId()
            + ", employeeId: "
            + projectAllocationUpdated.getEmployeeId()
            + " updated");

    return projectAllocationUpdated;
  }

  @DeleteMapping(path = "/{id}")
  public boolean delete(@PathVariable("id") long id) {

    boolean projectAllocationDeleted = projectAllocationServiceFacade.delete(id);
    log.info("ProjectAllocation id:" + id + " deleted");

    return projectAllocationDeleted;
  }

  @GetMapping(path = "/{id}")
  public ProjectAllocationDisplayDto fetch(@PathVariable("id") long id) {

    return projectAllocationServiceFacade.fetch(id);
  }

  @GetMapping
  public ProjectAllocationFetchAllResponse fetchAll(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "20") int limit,
      @RequestParam(defaultValue = "ID") ProjectAllocationSortProperty sortProperty,
      @RequestParam(defaultValue = "false") boolean sortDesc) {

    return projectAllocationServiceFacade.fetchAll(page, limit, sortProperty, sortDesc);
  }
}
