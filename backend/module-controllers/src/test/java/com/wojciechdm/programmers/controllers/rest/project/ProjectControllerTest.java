package com.wojciechdm.programmers.controllers.rest.project;

import static com.wojciechdm.programmers.company.structure.project.ProjectStatus.ACTIVE;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wojciechdm.programmers.company.structure.project.*;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.*;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProjectControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private DataSource dataSource;

  @BeforeEach
  void setUp() throws SQLException {
    ScriptUtils.executeSqlScript(
        dataSource.getConnection(), new ClassPathResource("schema-test.sql"));
  }

  @Test
  void shouldCrudProjectAndReturnProperStatusWhenInvalidRequest() throws Exception {

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
    ProjectSaveDto projectSaveDto =
        new ProjectSaveDto(
            "qwe", "asd", LocalDate.of(2019, 3, 15), LocalDate.of(2019, 5, 15), ACTIVE, 1L);
    // when
    ResultActions actualValidProjectSave =
        this.mockMvc.perform(
            post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8")
                .content(toJson(projectSaveDto))
                .header("Authorization", authorizationToken));
    // then
    actualValidProjectSave
        .andExpect(status().isCreated())
        .andExpect(jsonPath("id", is(2)))
        .andExpect(jsonPath("name", is(projectSaveDto.getName())))
        .andExpect(jsonPath("codeName", is(projectSaveDto.getCodeName())))
        .andExpect(jsonPath("startDate", is(projectSaveDto.getStartDate().toString())))
        .andExpect(jsonPath("endDate", is(projectSaveDto.getEndDate().toString())))
        .andExpect(jsonPath("status", is(projectSaveDto.getStatus().toString())))
        .andExpect(jsonPath("clientId", is(1)));

    // fetch
    // when
    ResultActions actualValidProjectFetch = this.mockMvc.perform(get("/api/projects/2"));
    // then
    actualValidProjectFetch
        .andExpect(status().isOk())
        .andExpect(jsonPath("id", is(2)))
        .andExpect(jsonPath("name", is(projectSaveDto.getName())))
        .andExpect(jsonPath("codeName", is(projectSaveDto.getCodeName())))
        .andExpect(jsonPath("startDate", is(projectSaveDto.getStartDate().toString())))
        .andExpect(jsonPath("endDate", is(projectSaveDto.getEndDate().toString())))
        .andExpect(jsonPath("status", is(projectSaveDto.getStatus().toString())))
        .andExpect(jsonPath("clientId", is(1)));

    // fetch all
    // when
    ResultActions actualValidProjectFetchAll =
        this.mockMvc.perform(get("/api/projects?page=2&limit=1"));
    // then
    actualValidProjectFetchAll
        .andExpect(status().isOk())
        .andExpect(jsonPath("data[0].id", is(2)))
        .andExpect(jsonPath("data[0].name", is(projectSaveDto.getName())))
        .andExpect(jsonPath("data[0].codeName", is(projectSaveDto.getCodeName())))
        .andExpect(jsonPath("data[0].startDate", is(projectSaveDto.getStartDate().toString())))
        .andExpect(jsonPath("data[0].endDate", is(projectSaveDto.getEndDate().toString())))
        .andExpect(jsonPath("data[0].status", is(projectSaveDto.getStatus().toString())))
        .andExpect(jsonPath("data[0].clientId", is(1)));

    // fetch project allocations
    // when
    ResultActions actualProjectAllocationsFetch =
        this.mockMvc.perform(get("/api/projects/1/projectallocations"));
    // then
    actualProjectAllocationsFetch
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
    ProjectSaveDto projectToUpdate =
        new ProjectSaveDto(
            "qwedf", "asdty", LocalDate.of(2019, 1, 10), LocalDate.of(2019, 6, 25), ACTIVE, 1L);
    // when
    ResultActions actualValidProjectUpdate =
        this.mockMvc.perform(
            put("/api/projects/2")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8")
                .content(toJson(projectToUpdate))
                .header("Authorization", authorizationToken));
    // then
    actualValidProjectUpdate
        .andExpect(status().isOk())
        .andExpect(jsonPath("id", is(2)))
        .andExpect(jsonPath("name", is(projectToUpdate.getName())))
        .andExpect(jsonPath("codeName", is(projectToUpdate.getCodeName())))
        .andExpect(jsonPath("startDate", is(projectToUpdate.getStartDate().toString())))
        .andExpect(jsonPath("endDate", is(projectToUpdate.getEndDate().toString())))
        .andExpect(jsonPath("status", is(projectToUpdate.getStatus().toString())))
        .andExpect(jsonPath("clientId", is(1)));

    // delete
    // when
    ResultActions actualProjectDelete =
        this.mockMvc.perform(delete("/api/projects/2").header("Authorization", authorizationToken));
    // then
    actualProjectDelete.andExpect(status().isOk()).andExpect(content().string("true"));

    // fetch not exists
    // when
    ResultActions actualProjectNotExistsFetch = this.mockMvc.perform(get("/api/projects/4"));
    // then
    actualProjectNotExistsFetch.andExpect(status().isNotFound());

    // save project with not unique code name
    // given
    ProjectSaveDto projectToSaveNotUniqueCodeName =
        new ProjectSaveDto(
            "qwedf", "uuu", LocalDate.of(2019, 1, 10), LocalDate.of(2019, 6, 25), ACTIVE, 2L);
    // when
    ResultActions actualProjectNotUniqueCodeNameSave =
        this.mockMvc.perform(
            post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8")
                .content(toJson(projectToSaveNotUniqueCodeName))
                .header("Authorization", authorizationToken));
    // then
    actualProjectNotUniqueCodeNameSave.andExpect(status().isConflict());

    // save project with not exists client id
    // given
    ProjectSaveDto projectToSaveNotExistsClientId =
        new ProjectSaveDto(
            "qwedf", "asdty", LocalDate.of(2019, 1, 10), LocalDate.of(2019, 6, 25), ACTIVE, 5L);
    // when
    ResultActions actualProjectNotExistsClientIdSave =
        this.mockMvc.perform(
            post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8")
                .content(toJson(projectToSaveNotExistsClientId))
                .header("Authorization", authorizationToken));
    // then
    actualProjectNotExistsClientIdSave.andExpect(status().isBadRequest());

    // save project with too long code name
    // given
    ProjectSaveDto projectToSaveTooLongCodeName =
        new ProjectSaveDto(
            "qwedf",
            "asdtyeeeeeerrrrrrrrrrrttttttttttttyyyyyyyy",
            LocalDate.of(2019, 1, 10),
            LocalDate.of(2019, 6, 25),
            ACTIVE,
            5L);
    // when
    ResultActions actualProjectTooLongCodeNameSave =
        this.mockMvc.perform(
            post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8")
                .content(toJson(projectToSaveTooLongCodeName))
                .header("Authorization", authorizationToken));
    // then
    actualProjectTooLongCodeNameSave.andExpect(status().isServiceUnavailable());

    // delete without authorization
    // when
    ResultActions actualProjectDeleteWithoutAuthorization =
        this.mockMvc.perform(delete("/api/projects/1"));
    // then
    actualProjectDeleteWithoutAuthorization.andExpect(status().isUnauthorized());
  }

  private String toJson(ProjectSaveDto project) {
    return "{\"name\":\""
        + project.getName()
        + "\",\"codeName\":\""
        + project.getCodeName()
        + "\",\"startDate\":\""
        + project.getStartDate().toString()
        + "\",\"endDate\":\""
        + project.getEndDate().toString()
        + "\",\"status\":\""
        + project.getStatus()
        + "\",\"clientId\":"
        + project.getClientId()
        + "}";
  }

  private String toJson(String username, String password) {
    return "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
  }
}
