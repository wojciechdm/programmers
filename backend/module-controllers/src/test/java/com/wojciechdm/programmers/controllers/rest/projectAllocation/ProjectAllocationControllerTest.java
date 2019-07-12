package com.wojciechdm.programmers.controllers.rest.projectAllocation;

import static com.wojciechdm.programmers.company.structure.employee.EmployeeLevel.*;
import static com.wojciechdm.programmers.company.structure.employee.EmployeeRole.*;
import static org.hamcrest.core.Is.is;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wojciechdm.programmers.company.structure.projectallocation.ProjectAllocationSaveDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProjectAllocationControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private DataSource dataSource;

  @BeforeEach
  void setUp() throws SQLException {
    ScriptUtils.executeSqlScript(
        dataSource.getConnection(), new ClassPathResource("schema-test.sql"));
  }

  @Test
  void shouldCrudProjectAllocationAndReturnProperStatusWhenInvalidRequest() throws Exception {

    // authorization for save, update and delete methods
    String username = "zzzzzz";
    String password = "zzzzzz";

    String loginResponse =
        this.mockMvc
            .perform(
                post("/api/login")
                    .contentType(APPLICATION_JSON_VALUE)
                    .characterEncoding("utf-8")
                    .content(toJson(username, password)))
            .andReturn()
            .getResponse()
            .getContentAsString();
    String authorizationToken =
        "Bearer " + loginResponse.substring(16, loginResponse.indexOf("\"", 17));

    // save
    // given
    ProjectAllocationSaveDto projectAllocationSaveDto =
        new ProjectAllocationSaveDto(
            1L,
            1L,
            LocalDate.of(2018, 11, 15),
            LocalDate.of(2019, 2, 8),
            10,
            null,
            ANALYST,
            JUNIOR,
            1500);
    // when
    ResultActions actualValidProjectAllocationSave =
        this.mockMvc.perform(
            post("/api/projectallocations")
                .contentType(APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8")
                .content(toJson(projectAllocationSaveDto))
                .header("Authorization", authorizationToken));
    // then
    actualValidProjectAllocationSave
        .andExpect(status().isCreated())
        .andExpect(jsonPath("id", is(2)))
        .andExpect(jsonPath("projectId", is(1)))
        .andExpect(jsonPath("employeeId", is(1)))
        .andExpect(jsonPath("startDate", is(projectAllocationSaveDto.getStartDate().toString())))
        .andExpect(jsonPath("endDate", is(projectAllocationSaveDto.getEndDate().toString())))
        .andExpect(
            jsonPath("percentileWorkload", is(projectAllocationSaveDto.getPercentileWorkload())))
        .andExpect(
            jsonPath(
                "hoursPerMonthWorkload", is(projectAllocationSaveDto.getHoursPerMonthWorkload())))
        .andExpect(jsonPath("role", is(projectAllocationSaveDto.getRole().toString())))
        .andExpect(jsonPath("level", is(projectAllocationSaveDto.getLevel().toString())))
        .andExpect(jsonPath("rateMonthly", is(projectAllocationSaveDto.getRateMonthly())));

    // fetch
    // when
    ResultActions actualValidProjectAllocationFetch =
        this.mockMvc.perform(get("/api/projectallocations/2"));
    // then
    actualValidProjectAllocationFetch
        .andExpect(status().isOk())
        .andExpect(jsonPath("id", is(2)))
        .andExpect(jsonPath("projectId", is(1)))
        .andExpect(jsonPath("employeeId", is(1)))
        .andExpect(jsonPath("startDate", is(projectAllocationSaveDto.getStartDate().toString())))
        .andExpect(jsonPath("endDate", is(projectAllocationSaveDto.getEndDate().toString())))
        .andExpect(
            jsonPath("percentileWorkload", is(projectAllocationSaveDto.getPercentileWorkload())))
        .andExpect(
            jsonPath(
                "hoursPerMonthWorkload", is(projectAllocationSaveDto.getHoursPerMonthWorkload())))
        .andExpect(jsonPath("role", is(projectAllocationSaveDto.getRole().toString())))
        .andExpect(jsonPath("level", is(projectAllocationSaveDto.getLevel().toString())))
        .andExpect(jsonPath("rateMonthly", is(projectAllocationSaveDto.getRateMonthly())));

    // fetch all
    // when
    ResultActions actualValidProjectAllocationsFetchAll =
        this.mockMvc.perform(get("/api/projectallocations?page=2&limit=1"));
    // then
    actualValidProjectAllocationsFetchAll
        .andExpect(status().isOk())
        .andExpect(jsonPath("data[0].id", is(2)))
        .andExpect(jsonPath("data[0].projectId", is(1)))
        .andExpect(jsonPath("data[0].employeeId", is(1)))
        .andExpect(
            jsonPath("data[0].startDate", is(projectAllocationSaveDto.getStartDate().toString())))
        .andExpect(
            jsonPath("data[0].endDate", is(projectAllocationSaveDto.getEndDate().toString())))
        .andExpect(
            jsonPath(
                "data[0].percentileWorkload", is(projectAllocationSaveDto.getPercentileWorkload())))
        .andExpect(
            jsonPath(
                "data[0].hoursPerMonthWorkload",
                is(projectAllocationSaveDto.getHoursPerMonthWorkload())))
        .andExpect(jsonPath("data[0].role", is(projectAllocationSaveDto.getRole().toString())))
        .andExpect(jsonPath("data[0].level", is(projectAllocationSaveDto.getLevel().toString())))
        .andExpect(jsonPath("data[0].rateMonthly", is(projectAllocationSaveDto.getRateMonthly())));

    // update
    // given
    ProjectAllocationSaveDto projectAllocationToUpdate =
        new ProjectAllocationSaveDto(
            1L,
            1L,
            LocalDate.of(2018, 11, 12),
            LocalDate.of(2019, 2, 3),
            null,
            20,
            ANALYST,
            JUNIOR,
            1800);
    // when
    ResultActions actualValidProjectAllocationUpdate =
        this.mockMvc.perform(
            put("/api/projectallocations/2")
                .contentType(APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8")
                .content(toJson(projectAllocationToUpdate))
                .header("Authorization", authorizationToken));
    // then
    actualValidProjectAllocationUpdate
        .andExpect(status().isOk())
        .andExpect(jsonPath("id", is(2)))
        .andExpect(jsonPath("projectId", is(1)))
        .andExpect(jsonPath("employeeId", is(1)))
        .andExpect(jsonPath("startDate", is(projectAllocationToUpdate.getStartDate().toString())))
        .andExpect(jsonPath("endDate", is(projectAllocationToUpdate.getEndDate().toString())))
        .andExpect(
            jsonPath("percentileWorkload", is(projectAllocationToUpdate.getPercentileWorkload())))
        .andExpect(
            jsonPath(
                "hoursPerMonthWorkload", is(projectAllocationToUpdate.getHoursPerMonthWorkload())))
        .andExpect(jsonPath("role", is(projectAllocationToUpdate.getRole().toString())))
        .andExpect(jsonPath("level", is(projectAllocationToUpdate.getLevel().toString())))
        .andExpect(jsonPath("rateMonthly", is(projectAllocationToUpdate.getRateMonthly())));

    // delete
    // when
    ResultActions actualProjectAllocationDelete =
        this.mockMvc.perform(
            delete("/api/projectallocations/2").header("Authorization", authorizationToken));
    // then
    actualProjectAllocationDelete.andExpect(status().isOk()).andExpect(content().string("true"));

    // fetch not exists
    // when
    ResultActions actualProjectAllocationNotExistsFetch =
        this.mockMvc.perform(get("/api/projectallocations/4"));
    // then
    actualProjectAllocationNotExistsFetch.andExpect(status().isNotFound());

    // save project allocation with invalid business rule
    // given
    ProjectAllocationSaveDto projectAllocationToSaveInvalidWorkload =
        new ProjectAllocationSaveDto(
            1L,
            1L,
            LocalDate.of(2018, 11, 12),
            LocalDate.of(2019, 2, 3),
            null,
            300,
            ANALYST,
            JUNIOR,
            1800);
    // when
    ResultActions actualProjectAllocationInvalidWorkloadSave =
        this.mockMvc.perform(
            post("/api/projectallocations")
                .contentType(APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8")
                .content(toJson(projectAllocationToSaveInvalidWorkload))
                .header("Authorization", authorizationToken));
    // then
    actualProjectAllocationInvalidWorkloadSave.andExpect(status().isConflict());

    // save project allocation with not exists project id
    // given
    ProjectAllocationSaveDto projectToSaveNotExistsProjectId =
        new ProjectAllocationSaveDto(
            4L,
            1L,
            LocalDate.of(2018, 11, 12),
            LocalDate.of(2019, 2, 3),
            null,
            10,
            ANALYST,
            JUNIOR,
            1800);
    // when
    ResultActions actualProjectNotExistsProjectIdSave =
        this.mockMvc.perform(
            post("/api/projectallocations")
                .contentType(APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8")
                .content(toJson(projectToSaveNotExistsProjectId))
                .header("Authorization", authorizationToken));
    // then
    actualProjectNotExistsProjectIdSave.andExpect(status().isNotFound());

    // delete without authorization
    // when
    ResultActions actualProjectAllocationDeleteWithoutAuthorization =
        this.mockMvc.perform(delete("/api/projectallocations/1"));
    // then
    actualProjectAllocationDeleteWithoutAuthorization.andExpect(status().isUnauthorized());
  }

  private String toJson(ProjectAllocationSaveDto projectAllocation) {
    return "{\"projectId\":"
        + projectAllocation.getProjectId()
        + ",\"employeeId\":"
        + projectAllocation.getEmployeeId()
        + ",\"startDate\":\""
        + projectAllocation.getStartDate().toString()
        + "\",\"endDate\":\""
        + projectAllocation.getEndDate().toString()
        + "\",\"percentileWorkload\":"
        + projectAllocation.getPercentileWorkload()
        + ",\"hoursPerMonthWorkload\":"
        + projectAllocation.getHoursPerMonthWorkload()
        + ",\"role\":\""
        + projectAllocation.getRole()
        + "\",\"level\":\""
        + projectAllocation.getLevel()
        + "\",\"rateMonthly\":"
        + projectAllocation.getRateMonthly()
        + "}";
  }

  private String toJson(String username, String password) {
    return "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
  }
}
