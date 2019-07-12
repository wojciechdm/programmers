package com.wojciechdm.programmers.company.structure.projectallocation;

class ProjectAllocationMapper {

  ProjectAllocationDisplayDto toDisplayDto(ProjectAllocation projectAllocation) {
    return new ProjectAllocationDisplayDto(
        projectAllocation.getId(),
        projectAllocation.getProjectId(),
        projectAllocation.getEmployeeId(),
        projectAllocation.getStartDate(),
        projectAllocation.getEndDate(),
        projectAllocation.getPercentileWorkload(),
        projectAllocation.getHoursPerMonthWorkload(),
        projectAllocation.getRole(),
        projectAllocation.getLevel(),
        projectAllocation.getRateMonthly());
  }

  ProjectAllocation toEntity(long id, ProjectAllocationSaveDto projectAllocationSaveDto) {
    return new ProjectAllocation(
        id,
        projectAllocationSaveDto.getProjectId(),
        projectAllocationSaveDto.getEmployeeId(),
        projectAllocationSaveDto.getStartDate(),
        projectAllocationSaveDto.getEndDate(),
        projectAllocationSaveDto.getPercentileWorkload(),
        projectAllocationSaveDto.getHoursPerMonthWorkload(),
        projectAllocationSaveDto.getRole(),
        projectAllocationSaveDto.getLevel(),
        projectAllocationSaveDto.getRateMonthly());
  }

  ProjectAllocation toEntity(ProjectAllocationSaveDto projectAllocationSaveDto) {
    return new ProjectAllocation(
        null,
        projectAllocationSaveDto.getProjectId(),
        projectAllocationSaveDto.getEmployeeId(),
        projectAllocationSaveDto.getStartDate(),
        projectAllocationSaveDto.getEndDate(),
        projectAllocationSaveDto.getPercentileWorkload(),
        projectAllocationSaveDto.getHoursPerMonthWorkload(),
        projectAllocationSaveDto.getRole(),
        projectAllocationSaveDto.getLevel(),
        projectAllocationSaveDto.getRateMonthly());
  }
}
