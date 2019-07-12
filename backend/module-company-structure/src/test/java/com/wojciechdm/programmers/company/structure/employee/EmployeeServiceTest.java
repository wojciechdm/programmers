package com.wojciechdm.programmers.company.structure.employee;

import static com.wojciechdm.programmers.company.structure.employee.EmployeeLevel.*;
import static com.wojciechdm.programmers.company.structure.employee.EmployeeRole.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

class EmployeeServiceTest {

  private EmployeeService employeeService;
  private EmployeeDao employeeDaoMock = mock(EmployeeDao.class);
  private EmployeeMapper employeeMapperMock = mock(EmployeeMapper.class);

  @BeforeEach
  void setUp() {
    employeeService = new EmployeeService(employeeDaoMock, employeeMapperMock);
  }

  @AfterEach
  void tearDown() {
    reset(employeeDaoMock, employeeMapperMock);
  }

  @Test
  void shouldThrowEmployeeNotFoundExceptionWhenUpdateNotExistsEmployee() {
    // given
    EmployeeSaveDto employeeSaveDto =
        new EmployeeSaveDto(
            "",
            "",
            "",
            LocalDate.now(),
            EmployeeStatus.EMPLOYED,
            Map.of(DEVELOPER, JUNIOR));
    Employee employee =
        new Employee(
            1L,
            "",
            "",
            "",
            LocalDate.now(),
            EmployeeStatus.EMPLOYED,
            Map.of(DEVELOPER, JUNIOR));
    when(employeeDaoMock.exists(1L)).thenReturn(false);
    when(employeeMapperMock.toEntity(1L, employeeSaveDto)).thenReturn(employee);
    Supplier<EmployeeDisplayDto> execute = () -> employeeService.update(1L, employeeSaveDto);
    Class expected = EmployeeNotFoundException.class;
    // when
    Class actual = assertThrows(EmployeeNotFoundException.class, execute::get).getClass();
    // then
    assertEquals(expected, actual);
  }

  @Test
  void shouldThrowEmployeeNotFoundExceptionWithCorrectMessageWhenUpdateNotExistsEmployee() {
    // given
    EmployeeSaveDto employeeSaveDto =
        new EmployeeSaveDto(
            "",
            "",
            "",
            LocalDate.now(),
            EmployeeStatus.EMPLOYED,
            Map.of(DEVELOPER, JUNIOR));
    Employee employee =
        new Employee(
            1L,
            "",
            "",
            "",
            LocalDate.now(),
            EmployeeStatus.EMPLOYED,
            Map.of(DEVELOPER, JUNIOR));
    when(employeeDaoMock.exists(1L)).thenReturn(false);
    when(employeeMapperMock.toEntity(1L, employeeSaveDto)).thenReturn(employee);
    Supplier<EmployeeDisplayDto> execute = () -> employeeService.update(1L, employeeSaveDto);
    String expected = "Employee id: 1 not found";
    // when
    String actual = assertThrows(EmployeeNotFoundException.class, execute::get).getMessage();
    // then
    assertEquals(expected, actual);
  }

  @Test
  void shouldThrowEmployeeNotFoundExceptionWhenFetchNotExistsEmployee() {
    // given
    when(employeeDaoMock.fetch(anyLong())).thenReturn(Optional.empty());
    when(employeeMapperMock.toDisplayDto(any())).thenReturn(any());
    Supplier<EmployeeDisplayDto> execute = () -> employeeService.fetch(1L);
    Class expected = EmployeeNotFoundException.class;
    // when
    Class actual = assertThrows(EmployeeNotFoundException.class, execute::get).getClass();
    // then
    assertEquals(expected, actual);
  }

  @Test
  void shouldThrowEmployeeNotFoundExceptionWithCorrectMessageWhenFetchNotExistsEmployee() {
    // given
    when(employeeDaoMock.fetch(anyLong())).thenReturn(Optional.empty());
    when(employeeMapperMock.toDisplayDto(any())).thenReturn(any());
    Supplier<EmployeeDisplayDto> execute = () -> employeeService.fetch(1L);
    String expected = "Employee id: 1 not found";
    // when
    String actual = assertThrows(EmployeeNotFoundException.class, execute::get).getMessage();
    // then
    assertEquals(expected, actual);
  }
}
