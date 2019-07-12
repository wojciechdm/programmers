package com.wojciechdm.programmers.company.structure.project;

import lombok.*;

@AllArgsConstructor
@Getter
public enum ProjectSortProperty {
  ID("id"),
  NAME("name"),
  CODE_NAME("code_name"),
  START_DATE("start_date"),
  END_DATE("end_date"),
  CREATE_DATE("create_date"),
  LAST_MODIFICATION_DATE("last_modification_date"),
  CLIENT("client_id");

  private String columnName;
}
