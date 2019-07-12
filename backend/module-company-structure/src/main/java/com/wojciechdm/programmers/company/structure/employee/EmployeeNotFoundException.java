package com.wojciechdm.programmers.company.structure.employee;

public class EmployeeNotFoundException extends RuntimeException {

  EmployeeNotFoundException(String message) {
    super(message);
  }
}
