package com.wojciechdm.programmers.controllers.rest.registration;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wojciechdm.programmers.user.registration.AddressSaveDto;
import com.wojciechdm.programmers.user.registration.UserLoginSaveDto;
import com.wojciechdm.programmers.user.registration.UserSaveDto;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RegistrationControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private DataSource dataSource;

  @BeforeEach
  void setUp() throws SQLException {
    ScriptUtils.executeSqlScript(
        dataSource.getConnection(), new ClassPathResource("schema-test.sql"));
  }

  @Test
  void shouldRegisterAndLoginUserAndReturnProperStatusWhenInvalidRequest() throws Exception {

    // save
    // given
    UserSaveDto userToSave =
        new UserSaveDto(
            new UserLoginSaveDto("ggg", new char[] {'h', 'h', 'h'}),
            "aaa",
            "bbb",
            20,
            LocalDate.of(1999, 10, 10),
            new AddressSaveDto("zzz", "ccc", "34/3", "23-999", "vvv"),
            "Pan");
    // when
    ResultActions actualValidUserSave =
        this.mockMvc.perform(
            post("/api/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8")
                .content(toJson(userToSave)));
    // then
    actualValidUserSave
        .andExpect(status().isCreated())
        .andExpect(jsonPath("firstName", is(userToSave.getFirstName())))
        .andExpect(jsonPath("lastName", is(userToSave.getLastName())))
        .andExpect(jsonPath("age", is(userToSave.getAge())))
        .andExpect(jsonPath("dateOfBirth", is(userToSave.getDateOfBirth().toString())))
        .andExpect(jsonPath("address.city", is(userToSave.getAddress().getCity())))
        .andExpect(jsonPath("address.street", is(userToSave.getAddress().getStreet())))
        .andExpect(jsonPath("address.number", is(userToSave.getAddress().getNumber())))
        .andExpect(jsonPath("address.zipcode", is(userToSave.getAddress().getZipcode())))
        .andExpect(jsonPath("address.country", is(userToSave.getAddress().getCountry())))
        .andExpect(jsonPath("title", is(userToSave.getTitle())));

    // login
    // given
    UserLoginSaveDto userLogin = new UserLoginSaveDto("ggg", new char[] {'h', 'h', 'h'});
    // when
    ResultActions actualValidUserLogin =
        this.mockMvc.perform(
            post("/api/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8")
                .content(toJson(userLogin)));
    // then
    actualValidUserLogin.andExpect(status().isOk());

    // save user with not unique username
    // given
    UserSaveDto userToSaveNotUniqueUsername =
        new UserSaveDto(
            new UserLoginSaveDto("ggg", new char[] {'h', 'h', 'h'}),
            "aaa",
            "bbb",
            20,
            LocalDate.of(1999, 10, 10),
            new AddressSaveDto(
                "zzz",
                "ccc",
                "34/3",
                "23-999",
                "vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv"),
            "Pan");
    // when
    ResultActions actualUserSaveNotUniqueUsername =
        this.mockMvc.perform(
            post("/api/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8")
                .content(toJson(userToSaveNotUniqueUsername)));
    // then
    actualUserSaveNotUniqueUsername.andExpect(status().isConflict());

    // save user with too long country
    // given
    UserSaveDto userToSaveTooLongCountry =
        new UserSaveDto(
            new UserLoginSaveDto("yyy", new char[] {'h', 'h', 'h'}),
            "aaa",
            "bbb",
            20,
            LocalDate.of(1999, 10, 10),
            new AddressSaveDto(
                "zzz",
                "ccc",
                "34/3",
                "23-999",
                "vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv"),
            "Pan");
    // when
    ResultActions actualUserSaveTooLongCountry =
        this.mockMvc.perform(
            post("/api/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8")
                .content(toJson(userToSaveTooLongCountry)));
    // then
    actualUserSaveTooLongCountry.andExpect(status().isServiceUnavailable());

    // login user with incorrect username or password
    // given
    UserLoginSaveDto userLoginIncorrectUsernamePassword =
        new UserLoginSaveDto("ggggg", new char[] {'h', 'h', 'h', 'h', 'h'});
    // when
    ResultActions actualUserLoginIncorrectUsernamePassword =
        this.mockMvc.perform(
            post("/api/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("utf-8")
                .content(toJson(userLoginIncorrectUsernamePassword)));
    // then
    actualUserLoginIncorrectUsernamePassword.andExpect(status().isUnauthorized());
  }

  private String toJson(UserSaveDto user) {
    return "{\"userLogin\":{\"username\":\""
        + user.getUserLogin().getUsername()
        + "\",\"password\":\""
        + new String(user.getUserLogin().getPassword())
        + "\"},\"firstName\":\""
        + user.getFirstName()
        + "\",\"lastName\":\""
        + user.getLastName()
        + "\",\"age\":"
        + user.getAge()
        + ",\"dateOfBirth\":\""
        + user.getDateOfBirth().toString()
        + "\",\"address\":{\"city\":\""
        + user.getAddress().getCity()
        + "\",\"street\":\""
        + user.getAddress().getStreet()
        + "\",\"number\":\""
        + user.getAddress().getNumber()
        + "\",\"zipcode\":\""
        + user.getAddress().getZipcode()
        + "\",\"country\":\""
        + user.getAddress().getCountry()
        + "\"},\"title\":\""
        + user.getTitle()
        + "\"}";
  }

  private String toJson(UserLoginSaveDto userLogin) {
    return "{\"username\":\""
        + userLogin.getUsername()
        + "\",\"password\":\""
        + new String(userLogin.getPassword())
        + "\"}";
  }
}
