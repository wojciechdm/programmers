package com.wojciechdm.programmers.company.structure.employee;

import lombok.*;

@Getter
@AllArgsConstructor
enum EmployeeSqlStatement {
  INSERT(
      "INSERT INTO employee(first_name, last_name, pesel, employment_date, status) VALUES(?, ?, ?, ?, ?)"),
  INSERT_ROLES("INSERT INTO employee_role(role, level, employee_id) VALUES(?, ?, ?)"),
  SELECT(
      "SELECT id, first_name, last_name, pesel, employment_date, status FROM employee WHERE id = ?"),
  SELECT_ALL("SELECT id, first_name, last_name, pesel, employment_date, status FROM employee"),
  LIMIT("LIMIT ?, ?"),
  SELECT_ROLES("SELECT role, level FROM employee_role WHERE employee_id = ?"),
  UPDATE(
      "UPDATE employee SET first_name = ?, last_name = ?, pesel = ?, employment_date = ?, status = ? WHERE id = ?"),
  DELETE("DELETE FROM employee WHERE id = ?"),
  DELETE_ROLES("DELETE FROM employee_role WHERE employee_id = ?"),
  COUNT("SELECT COUNT(id) FROM employee");

  private String statement;
}
