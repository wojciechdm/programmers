package com.wojciechdm.programmers.company.structure.projectallocation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.wojciechdm.programmers.company.structure.employee.EmployeeDisplayDto;
import com.wojciechdm.programmers.company.structure.employee.EmployeeServiceFacade;
import com.wojciechdm.programmers.company.structure.project.ProjectDisplayDto;
import com.wojciechdm.programmers.company.structure.project.ProjectServiceFacade;
import org.junit.jupiter.api.*;

import java.util.*;
import java.util.function.Supplier;

class ProjectAllocationServiceTest {

  private ProjectAllocationService projectAllocationService;
  private ProjectAllocationDao projectAllocationDaoMock = mock(ProjectAllocationDao.class);
  private ProjectAllocationMapper projectAllocationMapperMock = mock(ProjectAllocationMapper.class);
  private ProjectAllocationValidator projectAllocationValidatorMock =
      mock(ProjectAllocationValidator.class);
  private EmployeeServiceFacade employeeServiceMock = mock(EmployeeServiceFacade.class);
  private ProjectServiceFacade projectServiceMock = mock(ProjectServiceFacade.class);

  @BeforeEach
  void setUp() {
    projectAllocationService =
        new ProjectAllocationService(
            projectAllocationDaoMock,
            projectAllocationMapperMock,
            projectAllocationValidatorMock,
            employeeServiceMock,
            projectServiceMock);
  }

  @AfterEach
  void tearDown() {
    reset(
        projectAllocationDaoMock,
        projectAllocationMapperMock,
        projectAllocationValidatorMock,
        employeeServiceMock,
        projectServiceMock);
  }

  @Test
  void shouldThrowProjectAllocationValidationExceptionWhenSaveInvalidProjectAllocation() {
    // given
    ProjectAllocationSaveDto projectAllocationSaveDto =
        new ProjectAllocationSaveDto(1L, 1L, null, null, null, null, null, null, null);
    ProjectAllocation projectAllocation =
        new ProjectAllocation(1L, 1L, 1L, null, null, null, null, null, null, null);
    ProjectAllocationDisplayDto projectAllocationDisplayDto =
        new ProjectAllocationDisplayDto(1l, 1L, 1L, null, null, null, null, null, null, null);
    EmployeeDisplayDto employeeDisplayDto =
        new EmployeeDisplayDto(1L, null, null, null, null, null, null);
    ProjectDisplayDto projectDisplayDto =
        new ProjectDisplayDto(1L, null, null, null, null, null, null, null, null);
    when(projectAllocationDaoMock.save(any())).thenReturn(projectAllocation);
    when(projectAllocationMapperMock.toEntity(any())).thenReturn(projectAllocation);
    when(projectAllocationMapperMock.toDisplayDto(any())).thenReturn(projectAllocationDisplayDto);
    when(projectAllocationValidatorMock.validate(any(), anyList(), any(), any()))
        .thenReturn(
            ProjectAllocationValidationResult.getInstance(
                new EnumMap<>(Map.of(ProjectAllocationRuleViolation.INVALID_TIME_FRAME, true))));
    when(employeeServiceMock.fetch(anyLong())).thenReturn(employeeDisplayDto);
    when(projectServiceMock.fetch(anyLong())).thenReturn(projectDisplayDto);
    Supplier<ProjectAllocationDisplayDto> execute =
        () -> projectAllocationService.save(projectAllocationSaveDto);
    Class expected = ProjectAllocationValidationException.class;
    // when
    Class actual =
        assertThrows(ProjectAllocationValidationException.class, execute::get).getClass();
    // then
    assertEquals(expected, actual);
  }

  @Test
  void
      shouldThrowProjectAllocationValidationExceptionWithCorrectMessageWhenSaveInvalidProjectAllocation() {
    // given
    ProjectAllocationSaveDto projectAllocationSaveDto =
        new ProjectAllocationSaveDto(1L, 1L, null, null, null, null, null, null, null);
    ProjectAllocation projectAllocation =
        new ProjectAllocation(1L, 1L, 1L, null, null, null, null, null, null, null);
    ProjectAllocationDisplayDto projectAllocationDisplayDto =
        new ProjectAllocationDisplayDto(1l, 1L, 1L, null, null, null, null, null, null, null);
    EmployeeDisplayDto employeeDisplayDto =
        new EmployeeDisplayDto(1L, null, null, null, null, null, null);
    ProjectDisplayDto projectDisplayDto =
        new ProjectDisplayDto(1L, null, null, null, null, null, null, null, null);
    when(projectAllocationDaoMock.save(any())).thenReturn(projectAllocation);
    when(projectAllocationMapperMock.toEntity(any())).thenReturn(projectAllocation);
    when(projectAllocationMapperMock.toDisplayDto(any())).thenReturn(projectAllocationDisplayDto);
    when(projectAllocationValidatorMock.validate(any(), anyList(), any(), any()))
        .thenReturn(
            ProjectAllocationValidationResult.getInstance(
                new EnumMap<>(Map.of(ProjectAllocationRuleViolation.INVALID_TIME_FRAME, true))));
    when(employeeServiceMock.fetch(anyLong())).thenReturn(employeeDisplayDto);
    when(projectServiceMock.fetch(anyLong())).thenReturn(projectDisplayDto);
    Supplier<ProjectAllocationDisplayDto> execute =
        () -> projectAllocationService.save(projectAllocationSaveDto);
    String expected =
        "Start date of project allocation cannot be before start date of project and "
            + "end date of project allocation cannot be after end date of project; ";
    // when
    String actual =
        assertThrows(ProjectAllocationValidationException.class, execute::get).getMessage();
    // then
    assertEquals(expected, actual);
  }

  @Test
  void shouldThrowProjectAllocationValidationExceptionWhenUpdateInvalidProjectAllocation() {
    // given
    ProjectAllocationSaveDto projectAllocationSaveDto =
        new ProjectAllocationSaveDto(1L, 1L, null, null, null, null, null, null, null);
    ProjectAllocation projectAllocation =
        new ProjectAllocation(1L, 1L, 1L, null, null, null, null, null, null, null);
    ProjectAllocationDisplayDto projectAllocationDisplayDto =
        new ProjectAllocationDisplayDto(1l, 1L, 1L, null, null, null, null, null, null, null);
    EmployeeDisplayDto employeeDisplayDto =
        new EmployeeDisplayDto(1L, null, null, null, null, null, null);
    ProjectDisplayDto projectDisplayDto =
        new ProjectDisplayDto(1L, null, null, null, null, null, null, null, null);
    when(projectAllocationDaoMock.update(any())).thenReturn(projectAllocation);
    when(projectAllocationDaoMock.exists(anyLong())).thenReturn(true);
    when(projectAllocationMapperMock.toEntity(any())).thenReturn(projectAllocation);
    when(projectAllocationMapperMock.toDisplayDto(any())).thenReturn(projectAllocationDisplayDto);
    when(projectAllocationValidatorMock.validate(anyLong(), any(), anyList(), any(), any()))
        .thenReturn(
            ProjectAllocationValidationResult.getInstance(
                new EnumMap<>(Map.of(ProjectAllocationRuleViolation.INVALID_TIME_FRAME, true))));
    when(employeeServiceMock.fetch(anyLong())).thenReturn(employeeDisplayDto);
    when(projectServiceMock.fetch(anyLong())).thenReturn(projectDisplayDto);
    Supplier<ProjectAllocationDisplayDto> execute =
        () -> projectAllocationService.update(1L, projectAllocationSaveDto);
    Class expected = ProjectAllocationValidationException.class;
    // when
    Class actual =
        assertThrows(ProjectAllocationValidationException.class, execute::get).getClass();
    // then
    assertEquals(expected, actual);
  }

  @Test
  void
      shouldThrowProjectAllocationValidationExceptionWithCorrectMessageWhenUpdateInvalidProjectAllocation() {
    // given
    ProjectAllocationSaveDto projectAllocationSaveDto =
        new ProjectAllocationSaveDto(1L, 1L, null, null, null, null, null, null, null);
    ProjectAllocation projectAllocation =
        new ProjectAllocation(1L, 1L, 1L, null, null, null, null, null, null, null);
    ProjectAllocationDisplayDto projectAllocationDisplayDto =
        new ProjectAllocationDisplayDto(1l, 1L, 1L, null, null, null, null, null, null, null);
    EmployeeDisplayDto employeeDisplayDto =
        new EmployeeDisplayDto(1L, null, null, null, null, null, null);
    ProjectDisplayDto projectDisplayDto =
        new ProjectDisplayDto(1L, null, null, null, null, null, null, null, null);
    when(projectAllocationDaoMock.update(any())).thenReturn(projectAllocation);
    when(projectAllocationDaoMock.exists(anyLong())).thenReturn(true);
    when(projectAllocationMapperMock.toEntity(any())).thenReturn(projectAllocation);
    when(projectAllocationMapperMock.toDisplayDto(any())).thenReturn(projectAllocationDisplayDto);
    when(projectAllocationValidatorMock.validate(anyLong(), any(), anyList(), any(), any()))
        .thenReturn(
            ProjectAllocationValidationResult.getInstance(
                new EnumMap<>(Map.of(ProjectAllocationRuleViolation.INVALID_TIME_FRAME, true))));
    when(employeeServiceMock.fetch(anyLong())).thenReturn(employeeDisplayDto);
    when(projectServiceMock.fetch(anyLong())).thenReturn(projectDisplayDto);
    Supplier<ProjectAllocationDisplayDto> execute =
        () -> projectAllocationService.update(1L, projectAllocationSaveDto);
    String expected =
        "Start date of project allocation cannot be before start date of project and "
            + "end date of project allocation cannot be after end date of project; ";
    // when
    String actual =
        assertThrows(ProjectAllocationValidationException.class, execute::get).getMessage();
    // then
    assertEquals(expected, actual);
  }

  @Test
  void shouldThrowProjectAllocationNotFoundExceptionWhenUpdateNotExistsProjectAllocation() {
    // given
    ProjectAllocationSaveDto projectAllocationSaveDto =
        new ProjectAllocationSaveDto(1L, 1L, null, null, null, null, null, null, null);
    ProjectAllocation projectAllocation =
        new ProjectAllocation(1L, 1L, 1L, null, null, null, null, null, null, null);
    ProjectAllocationDisplayDto projectAllocationDisplayDto =
        new ProjectAllocationDisplayDto(1l, 1L, 1L, null, null, null, null, null, null, null);
    EmployeeDisplayDto employeeDisplayDto =
        new EmployeeDisplayDto(1L, null, null, null, null, null, null);
    ProjectDisplayDto projectDisplayDto =
        new ProjectDisplayDto(1L, null, null, null, null, null, null, null, null);
    when(projectAllocationDaoMock.update(any())).thenReturn(projectAllocation);
    when(projectAllocationDaoMock.exists(anyLong())).thenReturn(false);
    when(projectAllocationMapperMock.toEntity(any())).thenReturn(projectAllocation);
    when(projectAllocationMapperMock.toDisplayDto(any())).thenReturn(projectAllocationDisplayDto);
    when(projectAllocationValidatorMock.validate(any(), anyList(), any(), any()))
        .thenReturn(
            ProjectAllocationValidationResult.getInstance(
                new EnumMap<>(Map.of(ProjectAllocationRuleViolation.INVALID_TIME_FRAME, true))));
    when(employeeServiceMock.fetch(anyLong())).thenReturn(employeeDisplayDto);
    when(projectServiceMock.fetch(anyLong())).thenReturn(projectDisplayDto);
    Supplier<ProjectAllocationDisplayDto> execute =
        () -> projectAllocationService.update(1L, projectAllocationSaveDto);
    Class expected = ProjectAllocationNotFoundException.class;
    // when
    Class actual = assertThrows(ProjectAllocationNotFoundException.class, execute::get).getClass();
    // then
    assertEquals(expected, actual);
  }

  @Test
  void
      shouldThrowProjectAllocationNotFoundExceptionWithCorrectMessageWhenUpdateNotExistsProjectAllocation() {
    // given
    ProjectAllocationSaveDto projectAllocationSaveDto =
        new ProjectAllocationSaveDto(1L, 1L, null, null, null, null, null, null, null);
    ProjectAllocation projectAllocation =
        new ProjectAllocation(1L, 1L, 1L, null, null, null, null, null, null, null);
    ProjectAllocationDisplayDto projectAllocationDisplayDto =
        new ProjectAllocationDisplayDto(1l, 1L, 1L, null, null, null, null, null, null, null);
    EmployeeDisplayDto employeeDisplayDto =
        new EmployeeDisplayDto(1L, null, null, null, null, null, null);
    ProjectDisplayDto projectDisplayDto =
        new ProjectDisplayDto(1L, null, null, null, null, null, null, null, null);
    when(projectAllocationDaoMock.update(any())).thenReturn(projectAllocation);
    when(projectAllocationDaoMock.exists(anyLong())).thenReturn(false);
    when(projectAllocationMapperMock.toEntity(any())).thenReturn(projectAllocation);
    when(projectAllocationMapperMock.toDisplayDto(any())).thenReturn(projectAllocationDisplayDto);
    when(projectAllocationValidatorMock.validate(any(), anyList(), any(), any()))
        .thenReturn(
            ProjectAllocationValidationResult.getInstance(
                new EnumMap<>(Map.of(ProjectAllocationRuleViolation.INVALID_TIME_FRAME, true))));
    when(employeeServiceMock.fetch(anyLong())).thenReturn(employeeDisplayDto);
    when(projectServiceMock.fetch(anyLong())).thenReturn(projectDisplayDto);
    Supplier<ProjectAllocationDisplayDto> execute =
        () -> projectAllocationService.update(1L, projectAllocationSaveDto);
    String expected = "Project allocation id: 1 not found";
    // when
    String actual =
        assertThrows(ProjectAllocationNotFoundException.class, execute::get).getMessage();
    // then
    assertEquals(expected, actual);
  }

  @Test
  void shouldThrowProjectAllocationNotFoundExceptionWhenFetchNotExistsProjectAllocation() {
    // given
    ProjectAllocationSaveDto projectAllocationSaveDto =
        new ProjectAllocationSaveDto(1L, 1L, null, null, null, null, null, null, null);
    ProjectAllocationDisplayDto projectAllocationDisplayDto =
        new ProjectAllocationDisplayDto(1l, 1L, 1L, null, null, null, null, null, null, null);

    when(projectAllocationDaoMock.fetch(anyLong())).thenReturn(Optional.empty());
    when(projectAllocationMapperMock.toDisplayDto(any())).thenReturn(projectAllocationDisplayDto);
    Supplier<ProjectAllocationDisplayDto> execute =
        () -> projectAllocationService.update(1L, projectAllocationSaveDto);
    Class expected = ProjectAllocationNotFoundException.class;
    // when
    Class actual = assertThrows(ProjectAllocationNotFoundException.class, execute::get).getClass();
    // then
    assertEquals(expected, actual);
  }

  @Test
  void
      shouldThrowProjectAllocationNotFoundExceptionWithCorrectMessageWhenFetchNotExistsProjectAllocation() {
    // given
    ProjectAllocationSaveDto projectAllocationSaveDto =
        new ProjectAllocationSaveDto(1L, 1L, null, null, null, null, null, null, null);
    ProjectAllocationDisplayDto projectAllocationDisplayDto =
        new ProjectAllocationDisplayDto(1l, 1L, 1L, null, null, null, null, null, null, null);

    when(projectAllocationDaoMock.fetch(anyLong())).thenReturn(Optional.empty());
    when(projectAllocationMapperMock.toDisplayDto(any())).thenReturn(projectAllocationDisplayDto);
    Supplier<ProjectAllocationDisplayDto> execute =
        () -> projectAllocationService.update(1L, projectAllocationSaveDto);
    String expected = "Project allocation id: 1 not found";
    // when
    String actual =
        assertThrows(ProjectAllocationNotFoundException.class, execute::get).getMessage();
    // then
    assertEquals(expected, actual);
  }
}
