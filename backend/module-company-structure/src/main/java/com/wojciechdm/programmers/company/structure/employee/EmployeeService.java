package com.wojciechdm.programmers.company.structure.employee;

import lombok.AllArgsConstructor;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
public class EmployeeService {

  private EmployeeDao employeeDao;
  private EmployeeMapper employeeMapper;

  EmployeeDisplayDto save(EmployeeSaveDto employeeSaveDto) {
    return employeeMapper.toDisplayDto(employeeDao.save(employeeMapper.toEntity(employeeSaveDto)));
  }

  EmployeeDisplayDto update(long id, EmployeeSaveDto employeeSaveDto) {
    return Optional.of(employeeMapper.toEntity(id, employeeSaveDto))
        .filter(employee -> employeeDao.exists(employee.getId()))
        .map(employeeDao::update)
        .map(employeeMapper::toDisplayDto)
        .orElseThrow(() -> new EmployeeNotFoundException("Employee id: " + id + " not found"));
  }

  boolean delete(long id) {
    return employeeDao.delete(id);
  }

  public EmployeeDisplayDto fetch(long id) {
    return employeeDao
        .fetch(id)
        .map(employeeMapper::toDisplayDto)
        .orElseThrow(() -> new EmployeeNotFoundException("Employee id: " + id + " not found"));
  }

  EmployeeFetchAllResponse fetchAll(
      int page, int limit, EmployeeSortProperty sortProperty, boolean sortDesc) {
    return new EmployeeFetchAllResponse(
        employeeDao.count(),
        employeeDao.fetchAll(page, limit, sortProperty, sortDesc).stream()
            .map(employeeMapper::toDisplayDto)
            .collect(Collectors.toList()));
  }
}
