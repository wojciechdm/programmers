package com.wojciechdm.programmers.company.structure.client;

import static java.sql.Statement.*;

import lombok.AllArgsConstructor;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class ClientRepository implements ClientDao {

  private DataSource dataSource;

  @Override
  public Client save(Client client) {

    try (Connection connection = dataSource.getConnection();
         PreparedStatement preparedSelectCodeNameStatement =
            connection.prepareStatement(ClientSqlStatement.SELECT_CODE_NAME.getStatement());
         PreparedStatement preparedInsertStatement =
            connection.prepareStatement(ClientSqlStatement.INSERT.getStatement(), RETURN_GENERATED_KEYS);
         PreparedStatement preparedSelectStatement =
            connection.prepareStatement(ClientSqlStatement.SELECT.getStatement())) {

      connection.setAutoCommit(false);

      preparedSelectCodeNameStatement.setString(
          ClientSqlStatementParameter.SELECT_CODE_NAME_PARAM.getParameterNumber(), client.getCodeName());
      ResultSet resultSet = preparedSelectCodeNameStatement.executeQuery();

      if (resultSet.next()) {
        throw new ClientCodeNameNotUniqueException("Not unique code name of client during save");
      }

      prepareInsertStatement(client, preparedInsertStatement);
      preparedInsertStatement.executeUpdate();

      connection.commit();

      resultSet = preparedInsertStatement.getGeneratedKeys();
      resultSet.next();
      preparedSelectStatement.setLong(ClientSqlStatementParameter.SELECT_ID.getParameterNumber(), resultSet.getLong(1));
      resultSet = preparedSelectStatement.executeQuery();
      resultSet.next();

      return prepareClientToReturn(resultSet);

    } catch (SQLIntegrityConstraintViolationException e) {
      throw new ClientIllegalFieldValueException("Invalid value of client field during save", e);
    } catch (SQLException e) {
      throw new IllegalStateException("Problem with database", e);
    }
  }

  @Override
  public Client update(Client client) {

    try (Connection connection = dataSource.getConnection();
         PreparedStatement preparedSelectCodeNameStatement =
            connection.prepareStatement(ClientSqlStatement.SELECT_CODE_NAME.getStatement());
         PreparedStatement preparedUpdateStatement =
            connection.prepareStatement(ClientSqlStatement.UPDATE.getStatement());
         PreparedStatement preparedSelectStatement =
            connection.prepareStatement(ClientSqlStatement.SELECT.getStatement())) {

      connection.setAutoCommit(false);

      preparedSelectCodeNameStatement.setString(
          ClientSqlStatementParameter.SELECT_CODE_NAME_PARAM.getParameterNumber(), client.getCodeName());
      ResultSet resultSet = preparedSelectCodeNameStatement.executeQuery();

      if (resultSet.next() && resultSet.getLong(1) != client.getId()) {
        throw new ClientCodeNameNotUniqueException("Not unique code name of client during update");
      }

      prepareUpdateStatement(client, preparedUpdateStatement);
      preparedSelectStatement.setLong(ClientSqlStatementParameter.SELECT_ID.getParameterNumber(), client.getId());
      preparedUpdateStatement.executeUpdate();

      connection.commit();

      resultSet = preparedSelectStatement.executeQuery();
      resultSet.next();

      return prepareClientToReturn(resultSet);

    } catch (SQLIntegrityConstraintViolationException e) {
      throw new ClientIllegalFieldValueException("Invalid value of client field during update", e);
    } catch (SQLException e) {
      throw new IllegalStateException("Problem with database", e);
    }
  }

  @Override
  public boolean delete(long id) {

    try (Connection connection = dataSource.getConnection();
         PreparedStatement preparedStatementDelete =
            connection.prepareStatement(ClientSqlStatement.DELETE.getStatement());
         PreparedStatement preparedStatementSelect =
            connection.prepareStatement(ClientSqlStatement.SELECT.getStatement())) {

      preparedStatementDelete.setLong(ClientSqlStatementParameter.DELETE_ID.getParameterNumber(), id);
      preparedStatementSelect.setLong(ClientSqlStatementParameter.SELECT_ID.getParameterNumber(), id);
      preparedStatementDelete.executeUpdate();
      ResultSet resultSet = preparedStatementSelect.executeQuery();

      return !resultSet.next();

    } catch (SQLException e) {
      throw new IllegalStateException("Problem with database", e);
    }
  }

  @Override
  public Optional<Client> fetch(long id) {

    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(ClientSqlStatement.SELECT.getStatement())) {

      preparedStatement.setLong(ClientSqlStatementParameter.SELECT_ID.getParameterNumber(), id);
      ResultSet resultSet = preparedStatement.executeQuery();

      return resultSet.next() ? Optional.of(prepareClientToReturn(resultSet)) : Optional.empty();

    } catch (SQLException e) {
      throw new IllegalStateException("Problem with database", e);
    }
  }

  @Override
  public boolean exists(long id) {

    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(ClientSqlStatement.SELECT.getStatement())) {

      preparedStatement.setLong(ClientSqlStatementParameter.SELECT_ID.getParameterNumber(), id);
      ResultSet resultSet = preparedStatement.executeQuery();

      return resultSet.next();

    } catch (SQLException e) {
      throw new IllegalStateException("Problem with database", e);
    }
  }

  @Override
  public List<Client> fetchAll(
      int page, int limit, ClientSortProperty sortProperty, boolean sortDesc) {

    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedSelectStatement =
            connection.prepareStatement(
                ClientSqlStatement.SELECT_ALL.getStatement()
                    + " ORDER BY "
                    + sortProperty.getColumnName()
                    + (sortDesc ? " DESC " : " ")
                    + ClientSqlStatement.LIMIT.getStatement())) {

      preparedSelectStatement.setInt(ClientSqlStatementParameter.LIMIT_PAGE.getParameterNumber(), (page - 1) * limit);
      preparedSelectStatement.setInt(ClientSqlStatementParameter.LIMIT_ELEMENTS.getParameterNumber(), limit);
      ResultSet resultSet = preparedSelectStatement.executeQuery();
      List<Client> clients = new LinkedList<>();
      while (resultSet.next()) {
        clients.add(prepareClientToReturn(resultSet));
      }

      return clients;

    } catch (SQLException e) {
      throw new IllegalStateException("Problem with database", e);
    }
  }

  @Override
  public long count() {

    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(ClientSqlStatement.COUNT.getStatement())) {

      ResultSet resultSet = preparedStatement.executeQuery();
      resultSet.next();

      return resultSet.getLong(1);

    } catch (SQLException e) {
      throw new IllegalStateException("Problem with database", e);
    }
  }

  private void prepareInsertStatement(Client client, PreparedStatement preparedStatementInsert)
      throws SQLException {
    preparedStatementInsert.setString(ClientSqlStatementParameter.INSERT_NAME.getParameterNumber(), client.getName());
    preparedStatementInsert.setString(ClientSqlStatementParameter.INSERT_CODE_NAME.getParameterNumber(), client.getCodeName());
    preparedStatementInsert.setObject(
        ClientSqlStatementParameter.INSERT_KEY_ACCOUNT.getParameterNumber(), client.getKeyAccount());
    preparedStatementInsert.setDate(
        ClientSqlStatementParameter.INSERT_CREATE_DATE.getParameterNumber(), Date.valueOf(client.getCreateDate()));
    preparedStatementInsert.setDate(
        ClientSqlStatementParameter.INSERT_LAST_MODIFICATION_DATE.getParameterNumber(),
        Date.valueOf(client.getLastModificationDate()));
  }

  private void prepareUpdateStatement(Client client, PreparedStatement preparedUpdateStatement)
      throws SQLException {
    preparedUpdateStatement.setString(ClientSqlStatementParameter.UPDATE_NAME.getParameterNumber(), client.getName());
    preparedUpdateStatement.setString(ClientSqlStatementParameter.UPDATE_CODE_NAME.getParameterNumber(), client.getCodeName());
    preparedUpdateStatement.setObject(
        ClientSqlStatementParameter.UPDATE_KEY_ACCOUNT.getParameterNumber(), client.getKeyAccount());
    preparedUpdateStatement.setDate(
        ClientSqlStatementParameter.UPDATE_LAST_MODIFICATION_DATE.getParameterNumber(),
        Date.valueOf(client.getLastModificationDate()));
    preparedUpdateStatement.setLong(ClientSqlStatementParameter.UPDATE_ID.getParameterNumber(), client.getId());
  }

  private Client prepareClientToReturn(ResultSet resultSet) throws SQLException {
    return new Client(
        resultSet.getLong("id"),
        resultSet.getString("name"),
        resultSet.getString("code_name"),
        Optional.of(resultSet.getLong("key_account")).filter(id -> id != 0).orElse(null),
        resultSet.getDate("create_date").toLocalDate(),
        resultSet.getDate("last_modification_date").toLocalDate());
  }
}
