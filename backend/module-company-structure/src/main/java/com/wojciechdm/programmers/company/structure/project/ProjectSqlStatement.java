package com.wojciechdm.programmers.company.structure.project;

import lombok.*;

@Getter
@AllArgsConstructor
enum ProjectSqlStatement {
  INSERT(
      "INSERT INTO project(name, code_name, start_date, end_date, status, "
          + "create_date, last_modification_date, client_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?)"),
  SELECT(
      "SELECT id, name, code_name, start_date, end_date, status, create_date, last_modification_date, "
          + "client_id FROM project WHERE id = ?"),
  SELECT_CODE_NAME("SELECT id FROM project WHERE code_name= ? and client_id = ?"),
  SELECT_BY_CLIENT(
      "SELECT id, name, code_name, start_date, end_date, status, create_date, last_modification_date, "
          + "client_id FROM project WHERE client_id = ?"),
  SELECT_ALL(
      "SELECT id, name, code_name, start_date, end_date, status, create_date, last_modification_date, "
          + "client_id FROM project"),
  LIMIT("LIMIT ?, ?"),
  UPDATE(
      "UPDATE project SET name = ?, code_name = ?, start_date = ?, end_date = ?, "
          + "status = ?, last_modification_date = ?, client_id = ? WHERE id = ?"),
  DELETE("DELETE FROM project WHERE id = ?"),
  COUNT("SELECT COUNT(id) FROM project");

  String statement;
}
