package com.wojciechdm.programmers.user.registration;

import lombok.*;

@AllArgsConstructor
@Getter
enum UserSqlStatement {
  INSERT_USER(
      "INSERT INTO user(first_name, last_name, age, date_of_birth, address_id, title) VALUES (?, ?, ?, ?, ?, ?)"),
  INSERT_ADDRESS(
      "INSERT INTO address(city, street, number, zipcode, country) VALUES (?, ?, ?, ?, ?)"),
  SELECT_USER(
      "SELECT id, first_name, last_name, age, date_of_birth, address_id, title FROM user WHERE id = ?"),
  SELECT_ADDRESS("SELECT id, city, street, number, zipcode, country FROM address WHERE id = ?"),
  INSERT_USER_LOGIN("INSERT INTO user_login(username, password, user_data_id) VALUES (?, ?, ?)"),
  INSERT_USER_ROLE("INSERT INTO user_role(role, user_id) VALUES (?, ?)"),
  SELECT_USER_LOGIN_CHECK("SELECT id FROM user_login WHERE username = ?"),
  SELECT_USER_LOGIN_BY_USERNAME("SELECT id, username, password FROM user_login WHERE username = ?"),
  SELECT_USER_LOGIN_BY_ID("SELECT id, username, password FROM user_login WHERE id = ?"),
  SELECT_USER_LOGIN_ROLE("SELECT role FROM user_role WHERE user_id = ?");

  String statement;
}
