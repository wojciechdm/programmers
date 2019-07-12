package com.wojciechdm.programmers.controllers.rest.employee;

import com.wojciechdm.programmers.company.structure.employee.*;
import com.wojciechdm.programmers.company.structure.projectallocation.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/employees")
@Slf4j
public class EmployeeController {

  private final EmployeeServiceFacade employeeServiceFacade;

  private final ProjectAllocationServiceFacade projectAllocationServiceFacade;

  @ResponseStatus(CREATED)
  @PostMapping
  public EmployeeDisplayDto save(@RequestBody EmployeeSaveDto employeeSaveDto) {

    EmployeeDisplayDto employeeSaved = employeeServiceFacade.save(employeeSaveDto);
    log.info(
        "Employee id: "
            + employeeSaved.getId()
            + ", firstName: "
            + employeeSaved.getFirstName()
            + ", lastName: "
            + employeeSaved.getLastName()
            + " saved");

    return employeeSaved;
  }

  @PutMapping(path = "/{id}")
  public EmployeeDisplayDto update(
      @PathVariable("id") long id, @RequestBody EmployeeSaveDto employeeSaveDto) {

    EmployeeDisplayDto employeeUpdated = employeeServiceFacade.update(id, employeeSaveDto);
    log.info(
        "Employee id: "
            + employeeUpdated.getId()
            + ", firstName: "
            + employeeUpdated.getFirstName()
            + ", lastName: "
            + employeeUpdated.getLastName()
            + " updated");

    return employeeUpdated;
  }

  @DeleteMapping(path = "/{id}")
  public boolean delete(@PathVariable("id") long id) {

    boolean employeeDeleted = employeeServiceFacade.delete(id);
    log.info("Employee id:" + id + " deleted");

    return employeeDeleted;
  }

  @GetMapping(path = "/{id}")
  public EmployeeDisplayDto fetch(@PathVariable("id") long id) {

    return employeeServiceFacade.fetch(id);
  }

  @GetMapping(path = "/{id}/projectallocations")
  public List<ProjectAllocationDisplayDto> fetchEmployeeProjectAllocations(
      @PathVariable("id") long id) {

    return projectAllocationServiceFacade.fetchByEmployeeId(id);
  }

  @GetMapping
  public EmployeeFetchAllResponse fetchAll(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "20") int limit,
      @RequestParam(defaultValue = "ID") EmployeeSortProperty sortProperty,
      @RequestParam(defaultValue = "false") boolean sortDesc) {

    return employeeServiceFacade.fetchAll(page, limit, sortProperty, sortDesc);
  }
}
