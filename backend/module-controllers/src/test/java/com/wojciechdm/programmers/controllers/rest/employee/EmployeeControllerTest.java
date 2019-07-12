package com.wojciechdm.programmers.controllers.rest.employee;

import static com.wojciechdm.programmers.company.structure.employee.EmployeeLevel.*;
import static com.wojciechdm.programmers.company.structure.employee.EmployeeRole.*;
import static com.wojciechdm.programmers.company.structure.employee.EmployeeStatus.*;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wojciechdm.programmers.company.structure.employee.*;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EmployeeControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private DataSource dataSource;

  @BeforeEach
  void setUp() throws SQLException {
    ScriptUtils.executeSqlScript(
        dataSource.getConnection(), new ClassPathResource("schema-test.sql"));
  }

  @Test
  void shouldCrudEmployeeAndReturnProperStatusWhenInvalidRequest() throws Exception {

    // authorization for save, update and delete methods
    String username = "zzzzzz";
    String password = "zzzzzz";

    String loginResponse =
        this.mockMvc
            .perform(
                post("/api/login")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .characterEncoding("utf-8")
                    .content(toJson(username, password)))
            .andReturn()
            .getResponse()
            .getContentAsString();
    String authorizationToken =
        "Bearer " + loginResponse.substring(16, loginResponse.indexOf("\"", 17));

    // save
    // given
    EmployeeSaveDto employeeSaveDto =
        new EmployeeSaveDto(
            "qwe",
            "asd",
            "12345678901",
            LocalDate.of(2019, 5, 15),
            EMPLOYED,
            Map.of(DEVELOPER, JUNIOR, TESTER, SENIOR));
    // when
    ResultActions actualValidEmployeeSave =
        this.mockMvc.perform(
            post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8")
                .content(toJson(employeeSaveDto))
                .header("Authorization", authorizationToken));
    // then
    actualValidEmployeeSave
        .andExpect(status().isCreated())
        .andExpect(jsonPath("id", is(2)))
        .andExpect(jsonPath("firstName", is(employeeSaveDto.getFirstName())))
        .andExpect(jsonPath("lastName", is(employeeSaveDto.getLastName())))
        .andExpect(jsonPath("pesel", is(employeeSaveDto.getPesel())))
        .andExpect(jsonPath("employmentDate", is(employeeSaveDto.getEmploymentDate().toString())))
        .andExpect(jsonPath("status", is(employeeSaveDto.getStatus().toString())))
        .andExpect(jsonPath("roles.TESTER", is("SENIOR")))
        .andExpect(jsonPath("roles.DEVELOPER", is("JUNIOR")));

    // fetch
    // when
    ResultActions actualValidEmployeeFetch = this.mockMvc.perform(get("/api/employees/2"));
    // then
    actualValidEmployeeFetch
        .andExpect(status().isOk())
        .andExpect(jsonPath("id", is(2)))
        .andExpect(jsonPath("firstName", is(employeeSaveDto.getFirstName())))
        .andExpect(jsonPath("lastName", is(employeeSaveDto.getLastName())))
        .andExpect(jsonPath("pesel", is(employeeSaveDto.getPesel())))
        .andExpect(jsonPath("employmentDate", is(employeeSaveDto.getEmploymentDate().toString())))
        .andExpect(jsonPath("status", is(employeeSaveDto.getStatus().toString())))
        .andExpect(jsonPath("roles.TESTER", is("SENIOR")))
        .andExpect(jsonPath("roles.DEVELOPER", is("JUNIOR")));

    // fetch all
    // when
    ResultActions actualValidEmployeeFetchAll =
        this.mockMvc.perform(get("/api/employees?page=2&limit=1"));
    // then
    actualValidEmployeeFetchAll
        .andExpect(status().isOk())
        .andExpect(jsonPath("data[0].id", is(2)))
        .andExpect(jsonPath("data[0].firstName", is(employeeSaveDto.getFirstName())))
        .andExpect(jsonPath("data[0].lastName", is(employeeSaveDto.getLastName())))
        .andExpect(jsonPath("data[0].pesel", is(employeeSaveDto.getPesel())))
        .andExpect(
            jsonPath("data[0].employmentDate", is(employeeSaveDto.getEmploymentDate().toString())))
        .andExpect(jsonPath("data[0].status", is(employeeSaveDto.getStatus().toString())))
        .andExpect(jsonPath("data[0].roles.TESTER", is("SENIOR")))
        .andExpect(jsonPath("data[0].roles.DEVELOPER", is("JUNIOR")));

    // fetch employee project allocations
    // when
    ResultActions actualEmployeeProjectAllocationsFetch =
        this.mockMvc.perform(get("/api/employees/1/projectallocations"));
    // then
    actualEmployeeProjectAllocationsFetch
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].projectId", is(1)))
        .andExpect(jsonPath("$[0].employeeId", is(1)))
        .andExpect(jsonPath("$[0].startDate", is("2018-11-10")))
        .andExpect(jsonPath("$[0].endDate", is("2019-01-15")))
        .andExpect(jsonPath("$[0].percentileWorkload", is(100)))
        .andExpect(jsonPath("$[0].hoursPerMonthWorkload", IsNull.nullValue()))
        .andExpect(jsonPath("$[0].role", is("TESTER")))
        .andExpect(jsonPath("$[0].level", is("JUNIOR")))
        .andExpect(jsonPath("$[0].rateMonthly", is(2000)));

    // update
    // given
    EmployeeSaveDto employeeToUpdate =
        new EmployeeSaveDto(
            "tyu",
            "iop",
            "09876543210",
            LocalDate.of(2018, 4, 10),
            VACATION,
            Map.of(DEVELOPER, PROFESSIONAL, TESTER, JUNIOR));
    // when
    ResultActions actualValidEmployeeUpdate =
        this.mockMvc.perform(
            put("/api/employees/2")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8")
                .content(toJson(employeeToUpdate))
                .header("Authorization", authorizationToken));
    // then
    actualValidEmployeeUpdate
        .andExpect(status().isOk())
        .andExpect(jsonPath("id", is(2)))
        .andExpect(jsonPath("firstName", is(employeeToUpdate.getFirstName())))
        .andExpect(jsonPath("lastName", is(employeeToUpdate.getLastName())))
        .andExpect(jsonPath("pesel", is(employeeToUpdate.getPesel())))
        .andExpect(jsonPath("employmentDate", is(employeeToUpdate.getEmploymentDate().toString())))
        .andExpect(jsonPath("status", is(employeeToUpdate.getStatus().toString())))
        .andExpect(jsonPath("roles.TESTER", is("JUNIOR")))
        .andExpect(jsonPath("roles.DEVELOPER", is("PROFESSIONAL")));

    // delete
    // when
    ResultActions actualEmployeeDelete =
        this.mockMvc.perform(
            delete("/api/employees/2").header("Authorization", authorizationToken));
    // then
    actualEmployeeDelete.andExpect(status().isOk()).andExpect(content().string("true"));

    // fetch not exists
    // when
    ResultActions actualEmployeeNotExistsFetch = this.mockMvc.perform(get("/api/employees/4"));
    // then
    actualEmployeeNotExistsFetch.andExpect(status().isNotFound());

    // save employee with not exists key account
    // given
    EmployeeSaveDto employeeToSaveNullStatus =
        new EmployeeSaveDto(
            "tyu",
            "iop",
            "09876543210",
            LocalDate.of(2018, 4, 10),
            null,
            Map.of(DEVELOPER, PROFESSIONAL, TESTER, JUNIOR));
    // when
    ResultActions actualEmployeeNullStatusSave =
        this.mockMvc.perform(
            post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8")
                .content(toJson(employeeToSaveNullStatus))
                .header("Authorization", authorizationToken));
    // then
    actualEmployeeNullStatusSave.andExpect(status().isBadRequest());

    // save employee with too long pesel
    // given
    EmployeeSaveDto employeeToSaveTooLongPesel =
        new EmployeeSaveDto(
            "tyu",
            "iop",
            "09876543210123R",
            LocalDate.of(2018, 4, 10),
            VACATION,
            Map.of(DEVELOPER, PROFESSIONAL, TESTER, JUNIOR));
    // when
    ResultActions actualEmployeeTooLongPeselSave =
        this.mockMvc.perform(
            post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8")
                .content(toJson(employeeToSaveTooLongPesel))
                .header("Authorization", authorizationToken));
    // then
    actualEmployeeTooLongPeselSave.andExpect(status().isServiceUnavailable());

    // delete without authorization
    // when
    ResultActions actualEmployeeDeleteWithoutAuthorization =
        this.mockMvc.perform(delete("/api/employees/1"));
    // then
    actualEmployeeDeleteWithoutAuthorization.andExpect(status().isUnauthorized());
  }

  private String toJson(EmployeeSaveDto employee) {
    StringBuilder result =
        new StringBuilder(
            "{\"firstName\":\""
                + employee.getFirstName()
                + "\",\"lastName\":\""
                + employee.getLastName()
                + "\",\"pesel\":\""
                + employee.getPesel()
                + "\",\"employmentDate\":\""
                + employee.getEmploymentDate().toString()
                + "\",\"status\":\""
                + employee.getStatus()
                + "\",\"roles\":{");
    for (Iterator<Map.Entry<EmployeeRole, EmployeeLevel>> roles =
            employee.getRoles().entrySet().iterator();
        roles.hasNext(); ) {
      Map.Entry role = roles.next();
      result
          .append("\"")
          .append(role.getKey())
          .append("\":\"")
          .append(role.getValue())
          .append("\"")
          .append(roles.hasNext() ? "," : "");
    }
    result.append("}}");
    return result.toString();
  }

  private String toJson(String username, String password) {
    return "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
  }
}
