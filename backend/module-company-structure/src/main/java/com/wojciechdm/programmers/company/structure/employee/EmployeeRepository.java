package com.wojciechdm.programmers.company.structure.employee;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

import lombok.AllArgsConstructor;

import javax.sql.DataSource;
import java.sql.*;
import java.sql.Date;
import java.util.*;

@AllArgsConstructor
public class EmployeeRepository implements EmployeeDao {

  private DataSource dataSource;

  @Override
  public Employee save(Employee employee) {

    try (Connection connection = dataSource.getConnection();
         PreparedStatement preparedInsertStatement =
            connection.prepareStatement(EmployeeSqlStatement.INSERT.getStatement(), RETURN_GENERATED_KEYS);
         PreparedStatement preparedInsertRolesStatement =
            connection.prepareStatement(EmployeeSqlStatement.INSERT_ROLES.getStatement());
         PreparedStatement preparedSelectStatement =
            connection.prepareStatement(EmployeeSqlStatement.SELECT.getStatement());
         PreparedStatement preparedSelectRolesStatement =
            connection.prepareStatement(EmployeeSqlStatement.SELECT_ROLES.getStatement())) {

      connection.setAutoCommit(false);

      prepareInsertStatement(employee, preparedInsertStatement);
      preparedInsertStatement.executeUpdate();
      ResultSet resultSet = preparedInsertStatement.getGeneratedKeys();
      resultSet.next();
      preparedSelectStatement.setLong(EmployeeSqlStatementParameter.SELECT_ID.getParameterNumber(), resultSet.getLong(1));
      preparedInsertRolesStatement.setLong(
          EmployeeSqlStatementParameter.INSERT_ROLES_EMPLOYEE_ID.getParameterNumber(), resultSet.getLong(1));
      for (Map.Entry<EmployeeRole, EmployeeLevel> role : employee.getRoles().entrySet()) {
        preparedInsertRolesStatement.setString(
            EmployeeSqlStatementParameter.INSERT_ROLES_ROLE.getParameterNumber(), role.getKey().name());
        preparedInsertRolesStatement.setString(
            EmployeeSqlStatementParameter.INSERT_ROLES_LEVEL.getParameterNumber(), role.getValue().name());
        preparedInsertRolesStatement.executeUpdate();
      }

      connection.commit();

      preparedSelectRolesStatement.setLong(
          EmployeeSqlStatementParameter.SELECT_ROLES_EMPLOYEE_ID.getParameterNumber(), resultSet.getLong(1));
      ResultSet resultSetEmployee = preparedSelectStatement.executeQuery();
      ResultSet resultSetRoles = preparedSelectRolesStatement.executeQuery();

      resultSetEmployee.next();

      return prepareEmployeeToReturn(resultSetEmployee, resultSetRoles);

    } catch (SQLIntegrityConstraintViolationException e) {
      throw new EmployeeIllegalFieldValueException(
          "Invalid value of employee field during save", e);
    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public Employee update(Employee employee) {

    try (Connection connection = dataSource.getConnection();
         PreparedStatement preparedUpdateStatement =
            connection.prepareStatement(EmployeeSqlStatement.UPDATE.getStatement());
         PreparedStatement preparedDeleteRolesStatement =
            connection.prepareStatement(EmployeeSqlStatement.DELETE_ROLES.getStatement());
         PreparedStatement preparedInsertRolesStatement =
            connection.prepareStatement(EmployeeSqlStatement.INSERT_ROLES.getStatement());
         PreparedStatement preparedSelectStatement =
            connection.prepareStatement(EmployeeSqlStatement.SELECT.getStatement());
         PreparedStatement preparedSelectRolesStatement =
            connection.prepareStatement(EmployeeSqlStatement.SELECT_ROLES.getStatement())) {

      connection.setAutoCommit(false);

      prepareUpdateStatement(employee, preparedUpdateStatement);
      preparedDeleteRolesStatement.setLong(
          EmployeeSqlStatementParameter.DELETE_ROLES_EMPLOYEE_ID.getParameterNumber(), employee.getId());
      preparedSelectStatement.setLong(EmployeeSqlStatementParameter.SELECT_ID.getParameterNumber(), employee.getId());
      preparedInsertRolesStatement.setLong(
          EmployeeSqlStatementParameter.INSERT_ROLES_EMPLOYEE_ID.getParameterNumber(), employee.getId());
      preparedUpdateStatement.executeUpdate();
      preparedDeleteRolesStatement.executeUpdate();
      for (Map.Entry<EmployeeRole, EmployeeLevel> role : employee.getRoles().entrySet()) {
        preparedInsertRolesStatement.setString(
            EmployeeSqlStatementParameter.INSERT_ROLES_ROLE.getParameterNumber(), role.getKey().name());
        preparedInsertRolesStatement.setString(
            EmployeeSqlStatementParameter.INSERT_ROLES_LEVEL.getParameterNumber(), role.getValue().name());
        preparedInsertRolesStatement.executeUpdate();
      }

      connection.commit();

      preparedSelectRolesStatement.setLong(
          EmployeeSqlStatementParameter.SELECT_ROLES_EMPLOYEE_ID.getParameterNumber(), employee.getId());
      ResultSet resultSetEmployee = preparedSelectStatement.executeQuery();
      ResultSet resultSetRoles = preparedSelectRolesStatement.executeQuery();
      resultSetEmployee.next();

      return prepareEmployeeToReturn(resultSetEmployee, resultSetRoles);

    } catch (SQLIntegrityConstraintViolationException e) {
      throw new EmployeeIllegalFieldValueException(
          "Invalid value of employee field during update", e);
    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public boolean delete(long id) {

    try (Connection connection = dataSource.getConnection();
         PreparedStatement preparedStatementDelete =
            connection.prepareStatement(EmployeeSqlStatement.DELETE.getStatement());
         PreparedStatement preparedStatementSelect =
            connection.prepareStatement(EmployeeSqlStatement.SELECT.getStatement())) {

      preparedStatementDelete.setLong(EmployeeSqlStatementParameter.DELETE_ID.getParameterNumber(), id);
      preparedStatementSelect.setLong(EmployeeSqlStatementParameter.SELECT_ID.getParameterNumber(), id);
      preparedStatementDelete.executeUpdate();
      ResultSet resultSet = preparedStatementSelect.executeQuery();

      return !resultSet.next();

    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public Optional<Employee> fetch(long id) {

    try (Connection connection = dataSource.getConnection();
         PreparedStatement preparedStatementSelect =
            connection.prepareStatement(EmployeeSqlStatement.SELECT.getStatement());
         PreparedStatement preparedStatementSelectRoles =
            connection.prepareStatement(EmployeeSqlStatement.SELECT_ROLES.getStatement())) {

      preparedStatementSelect.setLong(EmployeeSqlStatementParameter.SELECT_ID.getParameterNumber(), id);
      preparedStatementSelectRoles.setLong(EmployeeSqlStatementParameter.SELECT_ROLES_EMPLOYEE_ID.getParameterNumber(), id);
      ResultSet resultSetEmployee = preparedStatementSelect.executeQuery();
      ResultSet resultSetRoles = preparedStatementSelectRoles.executeQuery();

      return resultSetEmployee.next()
          ? Optional.of(prepareEmployeeToReturn(resultSetEmployee, resultSetRoles))
          : Optional.empty();

    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public boolean exists(long id) {

    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(EmployeeSqlStatement.SELECT.getStatement())) {

      preparedStatement.setLong(EmployeeSqlStatementParameter.SELECT_ID.getParameterNumber(), id);
      ResultSet resultSet = preparedStatement.executeQuery();

      return resultSet.next();

    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public List<Employee> fetchAll(
      int page, int limit, EmployeeSortProperty sortProperty, boolean sortDesc) {

    try (Connection connection = dataSource.getConnection();
         PreparedStatement preparedSelectAllStatement =
            connection.prepareStatement(
                EmployeeSqlStatement.SELECT_ALL.getStatement()
                    + " ORDER BY "
                    + sortProperty.getColumnName()
                    + (sortDesc ? " DESC " : " ")
                    + EmployeeSqlStatement.LIMIT.getStatement());
         PreparedStatement preparedStatementSelectRoles =
            connection.prepareStatement(EmployeeSqlStatement.SELECT_ROLES.getStatement())) {

      connection.setAutoCommit(false);

      preparedSelectAllStatement.setInt(EmployeeSqlStatementParameter.LIMIT_PAGE.getParameterNumber(), (page - 1) * limit);
      preparedSelectAllStatement.setInt(EmployeeSqlStatementParameter.LIMIT_ELEMENTS.getParameterNumber(), limit);
      ResultSet resultSet = preparedSelectAllStatement.executeQuery();
      List<Employee> employees = new LinkedList<>();

      while (resultSet.next()) {
        preparedStatementSelectRoles.setLong(
            EmployeeSqlStatementParameter.SELECT_ROLES_EMPLOYEE_ID.getParameterNumber(), resultSet.getLong("id"));
        ResultSet resultSetRoles = preparedStatementSelectRoles.executeQuery();

        employees.add(prepareEmployeeToReturn(resultSet, resultSetRoles));
      }

      connection.commit();

      return employees;

    } catch (SQLException e) {
      throw new IllegalStateException("Problem with database", e);
    }
  }

  @Override
  public long count() {

    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(EmployeeSqlStatement.COUNT.getStatement())) {

      ResultSet resultSet = preparedStatement.executeQuery();
      resultSet.next();

      return resultSet.getLong(1);

    } catch (SQLException e) {
      throw new IllegalStateException("Problem with database", e);
    }
  }

  private void prepareInsertStatement(Employee employee, PreparedStatement preparedInsertStatement)
      throws SQLException {
    preparedInsertStatement.setString(
        EmployeeSqlStatementParameter.INSERT_FIRST_NAME.getParameterNumber(), employee.getFirstName());
    preparedInsertStatement.setString(
        EmployeeSqlStatementParameter.INSERT_LAST_NAME.getParameterNumber(), employee.getLastName());
    preparedInsertStatement.setString(EmployeeSqlStatementParameter.INSERT_PESEL.getParameterNumber(), employee.getPesel());
    preparedInsertStatement.setObject(
        EmployeeSqlStatementParameter.INSERT_EMPLOYMENT_DATE.getParameterNumber(),
        Optional.ofNullable(employee.getEmploymentDate()).map(Date::valueOf).orElse(null));
    preparedInsertStatement.setString(
        EmployeeSqlStatementParameter.INSERT_STATUS.getParameterNumber(), employee.getStatus().name());
  }

  private void prepareUpdateStatement(Employee employee, PreparedStatement preparedUpdateStatement)
      throws SQLException {
    preparedUpdateStatement.setString(
        EmployeeSqlStatementParameter.UPDATE_FIRST_NAME.getParameterNumber(), employee.getFirstName());
    preparedUpdateStatement.setString(
        EmployeeSqlStatementParameter.UPDATE_LAST_NAME.getParameterNumber(), employee.getLastName());
    preparedUpdateStatement.setString(EmployeeSqlStatementParameter.UPDATE_PESEL.getParameterNumber(), employee.getPesel());
    preparedUpdateStatement.setObject(
        EmployeeSqlStatementParameter.UPDATE_EMPLOYMENT_DATE.getParameterNumber(),
        Optional.ofNullable(employee.getEmploymentDate()).map(Date::valueOf).orElse(null));
    preparedUpdateStatement.setString(
        EmployeeSqlStatementParameter.UPDATE_STATUS.getParameterNumber(), employee.getStatus().name());
    preparedUpdateStatement.setLong(EmployeeSqlStatementParameter.UPDATE_ID.getParameterNumber(), employee.getId());
  }

  private Employee prepareEmployeeToReturn(ResultSet resultSetEmployee, ResultSet resultSetRoles)
      throws SQLException {
    Map<EmployeeRole, EmployeeLevel> roles = new HashMap<>();
    while (resultSetRoles.next()) {
      roles.put(
          EmployeeRole.valueOf(resultSetRoles.getString("role")),
          EmployeeLevel.valueOf(resultSetRoles.getString("level")));
    }

    return new Employee(
        resultSetEmployee.getLong("id"),
        resultSetEmployee.getString("first_name"),
        resultSetEmployee.getString("last_name"),
        resultSetEmployee.getString("pesel"),
        Optional.ofNullable(resultSetEmployee.getDate("employment_date"))
            .map(Date::toLocalDate)
            .orElse(null),
        EmployeeStatus.valueOf(resultSetEmployee.getString("status")),
        roles);
  }
}
