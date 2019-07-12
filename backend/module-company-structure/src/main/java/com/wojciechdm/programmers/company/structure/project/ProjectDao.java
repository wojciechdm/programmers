package com.wojciechdm.programmers.company.structure.project;

import java.util.*;

interface ProjectDao {

  Project save(Project project);

  Project update(Project project);

  boolean delete(long id);

  Optional<Project> fetch(long id);

  List<Project> fetchByClientId(long id);

  List<Project> fetchAll(int page, int limit, ProjectSortProperty sortProperty, boolean sortDesc);

  boolean exists(long id);

  long count();
}
