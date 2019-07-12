package com.wojciechdm.programmers.company.structure.project;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

import lombok.AllArgsConstructor;

import javax.sql.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;

@AllArgsConstructor
public class ProjectRepository implements ProjectDao {

  private DataSource dataSource;

  @Override
  public Project save(Project project) {

    try (Connection connection = dataSource.getConnection();
         PreparedStatement preparedSelectCodeNameStatement =
            connection.prepareStatement(ProjectSqlStatement.SELECT_CODE_NAME.getStatement());
         PreparedStatement preparedInsertStatement =
            connection.prepareStatement(ProjectSqlStatement.INSERT.getStatement(), RETURN_GENERATED_KEYS);
         PreparedStatement preparedSelectStatement =
            connection.prepareStatement(ProjectSqlStatement.SELECT.getStatement())) {

      connection.setAutoCommit(false);

      preparedSelectCodeNameStatement.setString(
          ProjectSqlStatementParameter.SELECT_CODE_NAME_NAME.getParameterNumber(), project.getCodeName());
      preparedSelectCodeNameStatement.setLong(
          ProjectSqlStatementParameter.SELECT_CODE_NAME_ID.getParameterNumber(), project.getClientId());
      ResultSet resultSet = preparedSelectCodeNameStatement.executeQuery();

      if (resultSet.next()) {
        throw new ProjectCodeNameNotUniqueForClientException(
            "Not unique code name for client during save");
      }

      prepareInsertStatement(project, preparedInsertStatement);
      preparedInsertStatement.executeUpdate();

      connection.commit();

      resultSet = preparedInsertStatement.getGeneratedKeys();
      resultSet.next();
      preparedSelectStatement.setLong(ProjectSqlStatementParameter.SELECT_ID.getParameterNumber(), resultSet.getLong(1));
      resultSet = preparedSelectStatement.executeQuery();
      resultSet.next();

      return prepareProjectToReturn(resultSet);

    } catch (SQLIntegrityConstraintViolationException e) {
      throw new ProjectIllegalFieldValueException("Invalid value of project field during save", e);
    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public Project update(Project project) {

    try (Connection connection = dataSource.getConnection();
         PreparedStatement preparedSelectCodeNameStatement =
            connection.prepareStatement(ProjectSqlStatement.SELECT_CODE_NAME.getStatement());
         PreparedStatement preparedUpdateStatement =
            connection.prepareStatement(ProjectSqlStatement.UPDATE.getStatement());
         PreparedStatement preparedSelectStatement =
            connection.prepareStatement(ProjectSqlStatement.SELECT.getStatement())) {

      connection.setAutoCommit(false);

      preparedSelectCodeNameStatement.setString(
          ProjectSqlStatementParameter.SELECT_CODE_NAME_NAME.getParameterNumber(), project.getCodeName());
      preparedSelectCodeNameStatement.setLong(
          ProjectSqlStatementParameter.SELECT_CODE_NAME_ID.getParameterNumber(), project.getClientId());
      ResultSet resultSet = preparedSelectCodeNameStatement.executeQuery();

      if (resultSet.next() && resultSet.getLong(1) != project.getId()) {
        throw new ProjectCodeNameNotUniqueForClientException(
            "Not unique code name for client during update");
      }

      prepareUpdateStatement(project, preparedUpdateStatement);
      preparedSelectStatement.setLong(ProjectSqlStatementParameter.SELECT_ID.getParameterNumber(), project.getId());
      preparedUpdateStatement.executeUpdate();

      connection.commit();

      resultSet = preparedSelectStatement.executeQuery();
      resultSet.next();

      return prepareProjectToReturn(resultSet);

    } catch (SQLIntegrityConstraintViolationException e) {
      throw new ProjectIllegalFieldValueException(
          "Invalid value of project field during update", e);
    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public boolean delete(long id) {

    try (Connection connection = dataSource.getConnection();
         PreparedStatement preparedDeleteStatement =
            connection.prepareStatement(ProjectSqlStatement.DELETE.getStatement());
         PreparedStatement preparedSelectStatement =
            connection.prepareStatement(ProjectSqlStatement.SELECT.getStatement())) {

      preparedDeleteStatement.setLong(ProjectSqlStatementParameter.DELETE_ID.getParameterNumber(), id);
      preparedSelectStatement.setLong(ProjectSqlStatementParameter.SELECT_ID.getParameterNumber(), id);
      preparedDeleteStatement.executeUpdate();
      ResultSet resultSet = preparedSelectStatement.executeQuery();

      return !resultSet.next();

    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public Optional<Project> fetch(long id) {

    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedSelectStatement =
            connection.prepareStatement(ProjectSqlStatement.SELECT.getStatement())) {

      preparedSelectStatement.setLong(ProjectSqlStatementParameter.SELECT_ID.getParameterNumber(), id);
      ResultSet resultSet = preparedSelectStatement.executeQuery();

      return resultSet.next() ? Optional.of(prepareProjectToReturn(resultSet)) : Optional.empty();

    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public List<Project> fetchByClientId(long id) {

    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedSelectStatement =
            connection.prepareStatement(ProjectSqlStatement.SELECT_BY_CLIENT.getStatement())) {

      preparedSelectStatement.setLong(ProjectSqlStatementParameter.SELECT_BY_CLIENT_ID.getParameterNumber(), id);
      ResultSet resultSet = preparedSelectStatement.executeQuery();
      List<Project> projects = new LinkedList<>();
      while (resultSet.next()) {
        projects.add(prepareProjectToReturn(resultSet));
      }

      return projects;

    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public List<Project> fetchAll(
      int page, int limit, ProjectSortProperty sortProperty, boolean sortDesc) {

    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedSelectStatement =
            connection.prepareStatement(
                ProjectSqlStatement.SELECT_ALL.getStatement()
                    + " ORDER BY "
                    + sortProperty.getColumnName()
                    + (sortDesc ? " DESC " : " ")
                    + ProjectSqlStatement.LIMIT.getStatement())) {

      preparedSelectStatement.setInt(ProjectSqlStatementParameter.LIMIT_PAGE.getParameterNumber(), (page - 1) * limit);
      preparedSelectStatement.setInt(ProjectSqlStatementParameter.LIMIT_ELEMENTS.getParameterNumber(), limit);
      ResultSet resultSet = preparedSelectStatement.executeQuery();
      List<Project> projects = new LinkedList<>();
      while (resultSet.next()) {
        projects.add(prepareProjectToReturn(resultSet));
      }

      return projects;

    } catch (SQLException e) {
      throw new IllegalStateException("Problem with database", e);
    }
  }

  @Override
  public boolean exists(long id) {

    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedSelectStatement =
            connection.prepareStatement(ProjectSqlStatement.SELECT.getStatement())) {

      preparedSelectStatement.setLong(ProjectSqlStatementParameter.SELECT_ID.getParameterNumber(), id);
      ResultSet resultSet = preparedSelectStatement.executeQuery();

      return resultSet.next();

    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public long count() {

    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(ProjectSqlStatement.COUNT.getStatement())) {

      ResultSet resultSet = preparedStatement.executeQuery();
      resultSet.next();

      return resultSet.getLong(1);

    } catch (SQLException e) {
      throw new IllegalStateException("Problem with database", e);
    }
  }

  private void prepareInsertStatement(Project project, PreparedStatement preparedStatementInsert)
      throws SQLException {
    preparedStatementInsert.setString(ProjectSqlStatementParameter.INSERT_NAME.getParameterNumber(), project.getName());
    preparedStatementInsert.setString(ProjectSqlStatementParameter.INSERT_CODE_NAME.getParameterNumber(), project.getCodeName());
    preparedStatementInsert.setDate(
        ProjectSqlStatementParameter.INSERT_START_DATE.getParameterNumber(), Date.valueOf(project.getStartDate()));
    preparedStatementInsert.setObject(
        ProjectSqlStatementParameter.INSERT_END_DATE.getParameterNumber(),
        Optional.ofNullable(project.getEndDate()).map(Date::valueOf).orElse(null));
    preparedStatementInsert.setString(
        ProjectSqlStatementParameter.INSERT_STATUS.getParameterNumber(), project.getStatus().name());
    preparedStatementInsert.setDate(
        ProjectSqlStatementParameter.INSERT_CREATE_DATE.getParameterNumber(), Date.valueOf(project.getCreateDate()));
    preparedStatementInsert.setDate(
        ProjectSqlStatementParameter.INSERT_LAST_MODIFICATION_DATE.getParameterNumber(),
        Date.valueOf(project.getLastModificationDate()));
    preparedStatementInsert.setLong(ProjectSqlStatementParameter.INSERT_CLIENT_ID.getParameterNumber(), project.getClientId());
  }

  private void prepareUpdateStatement(Project project, PreparedStatement preparedStatementUpdate)
      throws SQLException {
    preparedStatementUpdate.setString(ProjectSqlStatementParameter.UPDATE_NAME.getParameterNumber(), project.getName());
    preparedStatementUpdate.setString(ProjectSqlStatementParameter.UPDATE_CODE_NAME.getParameterNumber(), project.getCodeName());
    preparedStatementUpdate.setDate(
        ProjectSqlStatementParameter.UPDATE_START_DATE.getParameterNumber(), Date.valueOf(project.getStartDate()));
    preparedStatementUpdate.setObject(
        ProjectSqlStatementParameter.UPDATE_END_DATE.getParameterNumber(),
        Optional.ofNullable(project.getEndDate()).map(Date::valueOf).orElse(null));
    preparedStatementUpdate.setString(
        ProjectSqlStatementParameter.UPDATE_STATUS.getParameterNumber(), project.getStatus().name());
    preparedStatementUpdate.setDate(
        ProjectSqlStatementParameter.UPDATE_LAST_MODIFICATION_DATE.getParameterNumber(),
        Date.valueOf(project.getLastModificationDate()));
    preparedStatementUpdate.setLong(ProjectSqlStatementParameter.UPDATE_CLIENT_ID.getParameterNumber(), project.getClientId());
    preparedStatementUpdate.setLong(ProjectSqlStatementParameter.UPDATE_ID.getParameterNumber(), project.getId());
  }

  private Project prepareProjectToReturn(ResultSet resultSet) throws SQLException {
    return new Project(
        resultSet.getLong("id"),
        resultSet.getString("name"),
        resultSet.getString("code_name"),
        resultSet.getDate("start_date").toLocalDate(),
        Optional.ofNullable(resultSet.getDate("end_date")).map(Date::toLocalDate).orElse(null),
        ProjectStatus.valueOf(resultSet.getString("status")),
        resultSet.getDate("create_date").toLocalDate(),
        resultSet.getDate("last_modification_date").toLocalDate(),
        resultSet.getLong("client_id"));
  }
}
