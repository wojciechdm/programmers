package com.wojciechdm.programmers.company.structure.employee;

import java.util.*;

interface EmployeeDao {

  Employee save(Employee employee);

  Employee update(Employee employee);

  boolean delete(long id);

  Optional<Employee> fetch(long id);

  boolean exists(long id);

  List<Employee> fetchAll(int page, int limit, EmployeeSortProperty sortProperty, boolean sortDesc);

  long count();
}
