package com.wojciechdm.programmers.company.structure.client;

import lombok.*;

@AllArgsConstructor
@Getter
public enum ClientSortProperty {
  ID("id"),
  NAME("name"),
  CODE_NAME("code_name"),
  KEY_ACCOUNT("key_account"),
  CREATE_DATE("create_date"),
  LAST_MODIFICATION_DATE("last_modification_date");

  private String columnName;
}
