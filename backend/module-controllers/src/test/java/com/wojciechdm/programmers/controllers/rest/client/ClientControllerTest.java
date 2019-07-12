package com.wojciechdm.programmers.controllers.rest.client;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wojciechdm.programmers.company.structure.client.ClientSaveDto;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ClientControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private DataSource dataSource;

  @BeforeEach
  void setUp() throws SQLException {
    ScriptUtils.executeSqlScript(
        dataSource.getConnection(), new ClassPathResource("schema-test.sql"));
  }

  @Test
  void shouldCrudClientAndReturnProperStatusWhenInvalidRequest() throws Exception {

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
    ClientSaveDto clientToSave = new ClientSaveDto("aaa", "beebbrrr", null);
    // when
    ResultActions actualValidClientSave =
        this.mockMvc.perform(
            post("/api/clients")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8")
                .content(toJson(clientToSave))
                .header("Authorization", authorizationToken));
    // then
    actualValidClientSave
        .andExpect(status().isCreated())
        .andExpect(jsonPath("id", is(3)))
        .andExpect(jsonPath("name", is(clientToSave.getName())))
        .andExpect(jsonPath("codeName", is(clientToSave.getCodeName())))
        .andExpect(jsonPath("keyAccount", is(clientToSave.getKeyAccount())));

    // fetch
    // when
    ResultActions actualValidClientFetch = this.mockMvc.perform(get("/api/clients/3"));
    // then
    actualValidClientFetch
        .andExpect(status().isOk())
        .andExpect(jsonPath("id", is(3)))
        .andExpect(jsonPath("name", is(clientToSave.getName())))
        .andExpect(jsonPath("codeName", is(clientToSave.getCodeName())))
        .andExpect(jsonPath("keyAccount", is(clientToSave.getKeyAccount())));

    // fetch all
    // when
    ResultActions actualValidClientFetchAll =
        this.mockMvc.perform(get("/api/clients?page=3&limit=1"));
    // then
    actualValidClientFetchAll
        .andExpect(status().isOk())
        .andExpect(jsonPath("data[0].id", is(3)))
        .andExpect(jsonPath("data[0].name", is(clientToSave.getName())))
        .andExpect(jsonPath("data[0].codeName", is(clientToSave.getCodeName())))
        .andExpect(jsonPath("data[0].keyAccount", is(clientToSave.getKeyAccount())));

    // fetch client projects
    // when
    ResultActions actualClientProjectsFetch = this.mockMvc.perform(get("/api/clients/2/projects"));
    // then
    actualClientProjectsFetch
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].name", is("mmm")))
        .andExpect(jsonPath("$[0].codeName", is("uuu")))
        .andExpect(jsonPath("$[0].startDate", is("2018-10-14")))
        .andExpect(jsonPath("$[0].endDate", is("2019-02-10")))
        .andExpect(jsonPath("$[0].status", is("ACTIVE")))
        .andExpect(jsonPath("$[0].createDate", is("2019-04-25")))
        .andExpect(jsonPath("$[0].lastModificationDate", is("2019-04-26")))
        .andExpect(jsonPath("$[0].clientId", is(2)));

    // update
    // given
    ClientSaveDto clientToUpdate = new ClientSaveDto("bbb", "ccdddvvv", 1L);
    // when
    ResultActions actualValidClientUpdate =
        this.mockMvc.perform(
            put("/api/clients/3")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8")
                .content(toJson(clientToUpdate))
                .header("Authorization", authorizationToken));
    // then
    actualValidClientUpdate
        .andExpect(status().isOk())
        .andExpect(jsonPath("id", is(3)))
        .andExpect(jsonPath("name", is(clientToUpdate.getName())))
        .andExpect(jsonPath("codeName", is(clientToUpdate.getCodeName())))
        .andExpect(jsonPath("keyAccount", is(1)));

    // delete
    // when
    ResultActions actualClientDelete =
        this.mockMvc.perform(delete("/api/clients/3").header("Authorization", authorizationToken));
    // then
    actualClientDelete.andExpect(status().isOk()).andExpect(content().string("true"));

    // fetch not exists
    // when
    ResultActions actualClientNotExistsFetch = this.mockMvc.perform(get("/api/clients/4"));
    // then
    actualClientNotExistsFetch.andExpect(status().isNotFound());

    // save client with not unique code name
    // given
    ClientSaveDto clientToSaveNotUniqueCodeName = new ClientSaveDto("ccc", "xxx", null);
    // when
    ResultActions actualClientNotUniqueCodeNameSave =
        this.mockMvc.perform(
            post("/api/clients")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8")
                .content(toJson(clientToSaveNotUniqueCodeName))
                .header("Authorization", authorizationToken));
    // then
    actualClientNotUniqueCodeNameSave.andExpect(status().isConflict());

    // save client with not exists key account
    // given
    ClientSaveDto clientToSaveNotExistsKeyAccount = new ClientSaveDto("ccc", "ooo", 6L);
    // when
    ResultActions actualClientNotExistsKeyAccountSave =
        this.mockMvc.perform(
            post("/api/clients")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8")
                .content(toJson(clientToSaveNotExistsKeyAccount))
                .header("Authorization", authorizationToken));
    // then
    actualClientNotExistsKeyAccountSave.andExpect(status().isBadRequest());

    // save client with too long code name
    // given
    ClientSaveDto clientToSaveTooLongCodeName =
        new ClientSaveDto("rrr", "kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk", null);
    // when
    ResultActions actualClientTooLongCodeNameSave =
        this.mockMvc.perform(
            post("/api/clients")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8")
                .content(toJson(clientToSaveTooLongCodeName))
                .header("Authorization", authorizationToken));
    // then
    actualClientTooLongCodeNameSave.andExpect(status().isServiceUnavailable());

    // delete without authorization
    // when
    ResultActions actualClientDeleteWithoutAuthorization =
        this.mockMvc.perform(delete("/api/clients/2"));
    // then
    actualClientDeleteWithoutAuthorization.andExpect(status().isUnauthorized());
  }

  private String toJson(ClientSaveDto client) {
    return "{\"name\":\""
        + client.getName()
        + "\",\"codeName\":\""
        + client.getCodeName()
        + "\",\"keyAccount\":\""
        + client.getKeyAccount()
        + "\"}";
  }

  private String toJson(String username, String password) {
    return "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
  }
}
