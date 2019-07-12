package com.wojciechdm.programmers.company.structure.employee;

public class EmployeeIllegalFieldValueException extends RuntimeException {

  EmployeeIllegalFieldValueException(String message, Throwable cause) {
    super(message, cause);
  }
}
