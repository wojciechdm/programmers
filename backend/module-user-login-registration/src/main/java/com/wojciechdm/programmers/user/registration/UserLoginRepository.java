package com.wojciechdm.programmers.user.registration;

import lombok.AllArgsConstructor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class UserLoginRepository implements UserLoginDao {

  private DataSource dataSource;

  @Override
  public Optional<UserLogin> fetchByUsername(String username) {

    try (Connection connection = dataSource.getConnection();
         PreparedStatement preparedSelectUserLoginStatement =
            connection.prepareStatement(UserSqlStatement.SELECT_USER_LOGIN_BY_USERNAME.getStatement());
         PreparedStatement preparedSelectUserRoleStatement =
            connection.prepareStatement(UserSqlStatement.SELECT_USER_LOGIN_ROLE.getStatement())) {

      preparedSelectUserLoginStatement.setString(
          UserSqlStatementParameter.SELECT_USER_LOGIN_USERNAME.getParameterNumber(), username);
      ResultSet resultSetUserLogin = preparedSelectUserLoginStatement.executeQuery();

      if (resultSetUserLogin.next()) {
        preparedSelectUserRoleStatement.setLong(
            UserSqlStatementParameter.SELECT_USER_LOGIN_ID.getParameterNumber(), resultSetUserLogin.getLong("id"));
        ResultSet resultSetRole = preparedSelectUserRoleStatement.executeQuery();
        resultSetRole.next();
        return Optional.of(prepareUserLoginToReturn(resultSetUserLogin, resultSetRole));
      }
      return Optional.empty();

    } catch (SQLException e) {
      throw new IllegalStateException("Problem with database", e);
    }
  }

  @Override
  public Optional<UserLogin> fetchById(long id) {

    try (Connection connection = dataSource.getConnection();
         PreparedStatement preparedSelectUserLoginStatement =
            connection.prepareStatement(UserSqlStatement.SELECT_USER_LOGIN_BY_ID.getStatement());
         PreparedStatement preparedSelectUserRoleStatement =
            connection.prepareStatement(UserSqlStatement.SELECT_USER_LOGIN_ROLE.getStatement())) {

      preparedSelectUserRoleStatement.setLong(UserSqlStatementParameter.SELECT_USER_LOGIN_ID.getParameterNumber(), id);
      ResultSet resultSetRole = preparedSelectUserRoleStatement.executeQuery();
      resultSetRole.next();

      preparedSelectUserLoginStatement.setLong(UserSqlStatementParameter.SELECT_USER_LOGIN_ID.getParameterNumber(), id);
      ResultSet resultSetUserLogin = preparedSelectUserLoginStatement.executeQuery();

      return resultSetUserLogin.next()
          ? Optional.of(prepareUserLoginToReturn(resultSetUserLogin, resultSetRole))
          : Optional.empty();

    } catch (SQLException e) {
      throw new IllegalStateException("Problem with database", e);
    }
  }

  private UserLogin prepareUserLoginToReturn(ResultSet resultSetUserLogin, ResultSet resultSetRole)
      throws SQLException {

    return new UserLogin(
        resultSetUserLogin.getLong("id"),
        resultSetUserLogin.getString("username"),
        resultSetUserLogin.getString("password"),
        null,
        UserRole.valueOf(resultSetRole.getString("role")));
  }
}
