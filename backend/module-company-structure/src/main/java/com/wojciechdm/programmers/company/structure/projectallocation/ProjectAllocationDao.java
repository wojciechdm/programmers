package com.wojciechdm.programmers.company.structure.projectallocation;

import java.util.*;

interface ProjectAllocationDao {

  ProjectAllocation save(ProjectAllocation projectAllocation);

  ProjectAllocation update(ProjectAllocation projectAllocation);

  boolean delete(long id);

  Optional<ProjectAllocation> fetch(long id);

  List<ProjectAllocation> fetchByEmployeeId(long id);

  List<ProjectAllocation> fetchByProjectId(long id);

  List<ProjectAllocation> fetchAll(
      int page, int limit, ProjectAllocationSortProperty sortProperty, boolean sortDesc);

  boolean exists(long id);

  long count();
}
