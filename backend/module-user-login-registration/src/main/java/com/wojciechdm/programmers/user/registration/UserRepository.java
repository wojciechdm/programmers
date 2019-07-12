package com.wojciechdm.programmers.user.registration;

import lombok.AllArgsConstructor;

import javax.sql.DataSource;
import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@AllArgsConstructor
public class UserRepository implements UserDao {

  private DataSource dataSource;

  @Override
  public User save(User user) {

    try (Connection connection = dataSource.getConnection();
         PreparedStatement preparedSelectUserLoginStatement =
            connection.prepareStatement(UserSqlStatement.SELECT_USER_LOGIN_CHECK.getStatement());
         PreparedStatement preparedInsertUserStatement =
            connection.prepareStatement(UserSqlStatement.INSERT_USER.getStatement(), RETURN_GENERATED_KEYS);
         PreparedStatement preparedInsertAddressStatement =
            connection.prepareStatement(UserSqlStatement.INSERT_ADDRESS.getStatement(), RETURN_GENERATED_KEYS);
         PreparedStatement preparedInsertUserLoginStatement =
            connection.prepareStatement(UserSqlStatement.INSERT_USER_LOGIN.getStatement(), RETURN_GENERATED_KEYS);
         PreparedStatement preparedInsertUserRoleStatement =
            connection.prepareStatement(UserSqlStatement.INSERT_USER_ROLE.getStatement());
         PreparedStatement preparedSelectUserStatement =
            connection.prepareStatement(UserSqlStatement.SELECT_USER.getStatement());
         PreparedStatement preparedSelectAddressStatement =
            connection.prepareStatement(UserSqlStatement.SELECT_ADDRESS.getStatement())) {

      preparedSelectUserLoginStatement.setString(
              UserSqlStatementParameter.SELECT_USER_LOGIN_USERNAME.getParameterNumber(), user.getUserLogin().getUsername());
      ResultSet resultSetUsername = preparedSelectUserLoginStatement.executeQuery();

      if (resultSetUsername.next()) {
        throw new UsernameNotUniqueException("Not unique username");
      }

      connection.setAutoCommit(false);

      prepareInsertAddressStatement(user.getAddress(), preparedInsertAddressStatement);
      preparedInsertAddressStatement.executeUpdate();
      ResultSet resultSetAddressId = preparedInsertAddressStatement.getGeneratedKeys();
      resultSetAddressId.next();

      long addressId = resultSetAddressId.getLong(1);

      prepareInsertUserStatement(user, addressId, preparedInsertUserStatement);
      preparedInsertUserStatement.executeUpdate();
      ResultSet resultSetUserId = preparedInsertUserStatement.getGeneratedKeys();
      resultSetUserId.next();

      long userDataId = resultSetUserId.getLong(1);

      prepareInsertUserLoginStatement(
          user.getUserLogin(), userDataId, preparedInsertUserLoginStatement);
      preparedInsertUserLoginStatement.executeUpdate();
      ResultSet resultSetUserLoginId = preparedInsertUserLoginStatement.getGeneratedKeys();
      resultSetUserLoginId.next();

      long userLoginId = resultSetUserLoginId.getLong(1);

      prepareInsertUserRoleStatement(
          user.getUserLogin(), userLoginId, preparedInsertUserRoleStatement);
      preparedInsertUserRoleStatement.executeUpdate();

      connection.commit();

      preparedSelectAddressStatement.setLong(UserSqlStatementParameter.SELECT_ADDRESS_ID.getParameterNumber(), addressId);
      ResultSet resultSetAddress = preparedSelectAddressStatement.executeQuery();
      resultSetAddress.next();

      preparedSelectUserStatement.setLong(UserSqlStatementParameter.SELECT_USER_ID.getParameterNumber(), userDataId);
      ResultSet resultSetUser = preparedSelectUserStatement.executeQuery();
      resultSetUser.next();

      return prepareUserToReturn(resultSetUser, prepareAddressToReturn(resultSetAddress));

    } catch (SQLIntegrityConstraintViolationException e) {
      throw new UserIllegalFieldValueException("Invalid value of user field during save", e);
    } catch (SQLException e) {
      throw new IllegalStateException("Problem with database", e);
    }
  }

  private void prepareInsertAddressStatement(
      Address address, PreparedStatement preparedInsertAddressStatement) throws SQLException {
    preparedInsertAddressStatement.setString(
        UserSqlStatementParameter.INSERT_ADDRESS_CITY.getParameterNumber(), address.getCity());
    preparedInsertAddressStatement.setString(
        UserSqlStatementParameter.INSERT_ADDRESS_STREET.getParameterNumber(), address.getStreet());
    preparedInsertAddressStatement.setString(
        UserSqlStatementParameter.INSERT_ADDRESS_NUMBER.getParameterNumber(), address.getNumber());
    preparedInsertAddressStatement.setString(
        UserSqlStatementParameter.INSERT_ADDRESS_ZIPCODE.getParameterNumber(), address.getZipcode());
    preparedInsertAddressStatement.setString(
        UserSqlStatementParameter.INSERT_ADDRESS_COUNTRY.getParameterNumber(), address.getCountry());
  }

  private void prepareInsertUserStatement(
      User user, Long addressId, PreparedStatement preparedInsertUserStatement)
      throws SQLException {
    preparedInsertUserStatement.setString(
        UserSqlStatementParameter.INSERT_USER_FIRST_NAME.getParameterNumber(), user.getFirstName());
    preparedInsertUserStatement.setString(
        UserSqlStatementParameter.INSERT_USER_LAST_NAME.getParameterNumber(), user.getLastName());
    preparedInsertUserStatement.setInt(UserSqlStatementParameter.INSERT_USER_AGE.getParameterNumber(), user.getAge());
    preparedInsertUserStatement.setDate(
        UserSqlStatementParameter.INSERT_USER_DATE_OF_BIRTH.getParameterNumber(), Date.valueOf(user.getDateOfBirth()));
    preparedInsertUserStatement.setLong(UserSqlStatementParameter.INSERT_USER_ADDRESS_ID.getParameterNumber(), addressId);
    preparedInsertUserStatement.setString(UserSqlStatementParameter.INSERT_USER_TITLE.getParameterNumber(), user.getTitle());
  }

  private void prepareInsertUserLoginStatement(
      UserLogin userLogin, Long userId, PreparedStatement preparedInsertUserLoginStatement)
      throws SQLException {
    preparedInsertUserLoginStatement.setString(
        UserSqlStatementParameter.INSERT_USER_LOGIN_USERNAME.getParameterNumber(), userLogin.getUsername());
    preparedInsertUserLoginStatement.setString(
        UserSqlStatementParameter.INSERT_USER_LOGIN_PASSWORD.getParameterNumber(), userLogin.getPassword());
    preparedInsertUserLoginStatement.setLong(
        UserSqlStatementParameter.INSERT_USER_LOGIN_USER_DATA_ID.getParameterNumber(), userId);
  }

  private void prepareInsertUserRoleStatement(
      UserLogin userLogin, Long userLoginId, PreparedStatement preparedInsertUserRoleStatement)
      throws SQLException {
    preparedInsertUserRoleStatement.setString(
        UserSqlStatementParameter.INSERT_USER_ROLE_ROLE.getParameterNumber(), userLogin.getRole().name());
    preparedInsertUserRoleStatement.setLong(
        UserSqlStatementParameter.INSERT_USER_ROLE_USER_ID.getParameterNumber(), userLoginId);
  }

  private Address prepareAddressToReturn(ResultSet resultSet) throws SQLException {
    return new Address(
        resultSet.getLong("id"),
        resultSet.getString("city"),
        resultSet.getString("street"),
        resultSet.getString("number"),
        resultSet.getString("zipcode"),
        resultSet.getString("country"));
  }

  private User prepareUserToReturn(ResultSet resultSet, Address address) throws SQLException {
    return new User(
        resultSet.getLong("id"),
        null,
        resultSet.getString("first_name"),
        resultSet.getString("last_name"),
        resultSet.getInt("age"),
        resultSet.getDate("date_of_birth").toLocalDate(),
        address,
        resultSet.getString("title"));
  }
}
