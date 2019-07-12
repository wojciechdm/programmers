package com.wojciechdm.programmers.company.structure.employee;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.*;

public class EmployeeServiceFacade {

  private EmployeeService employeeService;

  public EmployeeServiceFacade(EmployeeDao employeeDao) {
    employeeService = new EmployeeService(employeeDao, new EmployeeMapper());
  }

  public EmployeeDisplayDto save(EmployeeSaveDto employeeSaveDto) {

    checkArgument(nonNull(employeeSaveDto), "Employee can not be null");

    return employeeService.save(employeeSaveDto);
  }

  public EmployeeDisplayDto update(long id, EmployeeSaveDto employeeSaveDto) {

    checkArgument(nonNull(employeeSaveDto), "Employee can not be null");

    return employeeService.update(id, employeeSaveDto);
  }

  public boolean delete(long id) {

    return employeeService.delete(id);
  }

  public EmployeeDisplayDto fetch(long id) {

    return employeeService.fetch(id);
  }

  public EmployeeFetchAllResponse fetchAll(
      int page, int limit, EmployeeSortProperty sortProperty, boolean sortDesc) {

    return employeeService.fetchAll(page, limit, sortProperty, sortDesc);
  }
}
