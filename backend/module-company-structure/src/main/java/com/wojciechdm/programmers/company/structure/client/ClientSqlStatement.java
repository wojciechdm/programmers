package com.wojciechdm.programmers.company.structure.client;

import lombok.*;

@Getter
@AllArgsConstructor
enum ClientSqlStatement {
  INSERT(
      "INSERT INTO client(name, code_name, key_account, create_date, last_modification_date) VALUES(?, ?, ?, ?, ?)"),
  SELECT(
      "SELECT id, name, code_name, key_account, create_date, last_modification_date FROM client WHERE id = ?"),
  UPDATE(
      "UPDATE client SET name = ?, code_name = ?, key_account = ?, last_modification_date = ? WHERE id = ?"),
  DELETE("DELETE FROM client WHERE id = ?"),
  SELECT_CODE_NAME("SELECT id FROM client WHERE code_name = ?"),
  SELECT_ALL(
      "SELECT id, name, code_name, key_account, create_date, last_modification_date FROM client"),
  LIMIT("LIMIT ?, ?"),
  COUNT("SELECT COUNT(id) FROM client");

  private String statement;
}
