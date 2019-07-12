package com.wojciechdm.programmers.company.structure.client;

import lombok.*;

@Getter
@AllArgsConstructor
enum ClientSqlStatementParameter {
  INSERT_NAME(1),
  INSERT_CODE_NAME(2),
  INSERT_KEY_ACCOUNT(3),
  INSERT_CREATE_DATE(4),
  INSERT_LAST_MODIFICATION_DATE(5),
  SELECT_ID(1),
  UPDATE_NAME(1),
  UPDATE_CODE_NAME(2),
  UPDATE_KEY_ACCOUNT(3),
  UPDATE_LAST_MODIFICATION_DATE(4),
  UPDATE_ID(5),
  DELETE_ID(1),
  SELECT_CODE_NAME_PARAM(1),
  LIMIT_PAGE(1),
  LIMIT_ELEMENTS(2);

  private int parameterNumber;
}
