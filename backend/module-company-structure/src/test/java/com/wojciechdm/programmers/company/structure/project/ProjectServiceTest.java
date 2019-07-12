package com.wojciechdm.programmers.company.structure.project;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Supplier;

class ProjectServiceTest {

  private ProjectService projectService;
  private ProjectDao projectDaoMock = mock(ProjectDao.class);
  private ProjectMapper projectMapperMock = mock(ProjectMapper.class);

  @BeforeEach
  void setUp() {
    projectService = new ProjectService(projectDaoMock, projectMapperMock);
  }

  @AfterEach
  void tearDown() {
    reset(projectDaoMock, projectMapperMock);
  }

  @Test
  void shouldThrowProjectNotFoundExceptionWhenUpdateNotExistsProject() {
    // given
    ProjectSaveDto projectSaveDto =
        new ProjectSaveDto("", "", LocalDate.now(), LocalDate.now(), ProjectStatus.ACTIVE, 1L);
    Project project =
        new Project(
            1L,
            "",
            "",
            LocalDate.now(),
            LocalDate.now(),
            ProjectStatus.ACTIVE,
            LocalDate.now(),
            LocalDate.now(),
            1L);
    when(projectDaoMock.exists(1L)).thenReturn(false);
    when(projectMapperMock.toEntity(1L, projectSaveDto)).thenReturn(project);
    Supplier<ProjectDisplayDto> execute = () -> projectService.update(1L, projectSaveDto);
    Class expected = ProjectNotFoundException.class;
    // when
    Class actual = assertThrows(ProjectNotFoundException.class, execute::get).getClass();
    // then
    assertEquals(expected, actual);
  }

  @Test
  void shouldThrowProjectNotFoundExceptionWithCorrectMessageWhenUpdateNotExistsProject() {
    // given
    ProjectSaveDto projectSaveDto =
        new ProjectSaveDto("", "", LocalDate.now(), LocalDate.now(), ProjectStatus.ACTIVE, 1L);
    Project project =
        new Project(
            1L,
            "",
            "",
            LocalDate.now(),
            LocalDate.now(),
            ProjectStatus.ACTIVE,
            LocalDate.now(),
            LocalDate.now(),
            1L);
    when(projectDaoMock.exists(1L)).thenReturn(false);
    when(projectMapperMock.toEntity(1L, projectSaveDto)).thenReturn(project);
    Supplier<ProjectDisplayDto> execute = () -> projectService.update(1L, projectSaveDto);
    String expected = "Project id: 1 not found";
    // when
    String actual = assertThrows(ProjectNotFoundException.class, execute::get).getMessage();
    // then
    assertEquals(expected, actual);
  }

  @Test
  void shouldThrowProjectNotFoundExceptionWhenFetchNotExistsProject() {
    // given
    when(projectDaoMock.fetch(anyLong())).thenReturn(Optional.empty());
    when(projectMapperMock.toDisplayDto(any())).thenReturn(any());
    Supplier<ProjectDisplayDto> execute = () -> projectService.fetch(1L);
    Class expected = ProjectNotFoundException.class;
    // when
    Class actual = assertThrows(ProjectNotFoundException.class, execute::get).getClass();
    // then
    assertEquals(expected, actual);
  }

  @Test
  void shouldThrowProjectNotFoundExceptionWithCorrectMessageWhenFetchNotExistsProject() {
    // given
    when(projectDaoMock.fetch(anyLong())).thenReturn(Optional.empty());
    when(projectMapperMock.toDisplayDto(any())).thenReturn(any());
    Supplier<ProjectDisplayDto> execute = () -> projectService.fetch(1L);
    String expected = "Project id: 1 not found";
    // when
    String actual = assertThrows(ProjectNotFoundException.class, execute::get).getMessage();
    // then
    assertEquals(expected, actual);
  }
}
