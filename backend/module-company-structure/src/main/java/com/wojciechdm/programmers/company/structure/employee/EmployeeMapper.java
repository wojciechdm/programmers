package com.wojciechdm.programmers.company.structure.employee;

class EmployeeMapper {

  EmployeeDisplayDto toDisplayDto(Employee employee) {
    return new EmployeeDisplayDto(
        employee.getId(),
        employee.getFirstName(),
        employee.getLastName(),
        employee.getPesel(),
        employee.getEmploymentDate(),
        employee.getStatus(),
        employee.getRoles());
  }

  Employee toEntity(long id, EmployeeSaveDto employeeSaveDto) {
    return new Employee(
            id,
            employeeSaveDto.getFirstName(),
            employeeSaveDto.getLastName(),
            employeeSaveDto.getPesel(),
            employeeSaveDto.getEmploymentDate(),
            employeeSaveDto.getStatus(),
            employeeSaveDto.getRoles());
  }

  Employee toEntity(EmployeeSaveDto employeeSaveDto) {
    return new Employee(
        null,
        employeeSaveDto.getFirstName(),
        employeeSaveDto.getLastName(),
        employeeSaveDto.getPesel(),
        employeeSaveDto.getEmploymentDate(),
        employeeSaveDto.getStatus(),
        employeeSaveDto.getRoles());
  }
}
