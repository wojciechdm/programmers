package com.wojciechdm.programmers.company.structure.employee;

import static java.util.Objects.*;

import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
class EmployeeJpaRepository implements EmployeeDao {

  private EntityManagerFactory entityManagerFactory;

  @Override
  public Employee save(Employee employee) {
    try {
      EntityManager entityManager = entityManagerFactory.createEntityManager();
      entityManager.getTransaction().begin();
      Employee employeeSaved = entityManager.merge(employee);
      entityManager.getTransaction().commit();
      entityManager.close();
      return employeeSaved;
    } catch (PersistenceException e) {
      if ((e.getCause() instanceof ConstraintViolationException)) {
        throw new EmployeeIllegalFieldValueException(
            "Invalid value of employee field during save", e);
      } else {
        throw new IllegalStateException(e);
      }
    }
  }

  @Override
  public Employee update(Employee employee) {
    try {
      EntityManager entityManager = entityManagerFactory.createEntityManager();
      entityManager.getTransaction().begin();
      Employee employeeUpdated = entityManager.merge(employee);
      entityManager.getTransaction().commit();
      entityManager.close();
      return employeeUpdated;
    } catch (PersistenceException e) {
      if ((e.getCause() instanceof ConstraintViolationException)) {
        throw new EmployeeIllegalFieldValueException(
            "Invalid value of employee field during update", e);
      } else {
        throw new IllegalStateException(e);
      }
    }
  }

  @Override
  public boolean delete(long id) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    Optional.ofNullable(entityManager.find(Employee.class, id)).ifPresent(entityManager::remove);
    Employee employee = entityManager.find(Employee.class, id);
    entityManager.getTransaction().commit();
    entityManager.close();
    return isNull(employee);
  }

  @Override
  public Optional<Employee> fetch(long id) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    Optional<Employee> employee = Optional.ofNullable(entityManager.find(Employee.class, id));
    entityManager.close();
    return employee;
  }

  @Override
  public boolean exists(long id) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    Employee employee = entityManager.find(Employee.class, id);
    entityManager.close();
    return nonNull(employee);
  }

  @Override
  public List<Employee> fetchAll(
      int page, int limit, EmployeeSortProperty sortProperty, boolean sortDesc) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    TypedQuery<Employee> typedQuery =
        entityManager.createQuery(
            "SELECT e FROM Employee AS e ORDER BY "
                + sortProperty.getColumnName()
                + (sortDesc ? " DESC" : ""),
            Employee.class);
    typedQuery.setFirstResult((page - 1) * limit);
    typedQuery.setMaxResults(limit);
    List<Employee> employees =
        Optional.ofNullable(typedQuery.getResultList()).orElse(Collections.emptyList());
    entityManager.close();
    return employees;
  }

  @Override
  public long count() {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    TypedQuery<Long> typedQuery =
        entityManager.createQuery("SELECT COUNT(*) FROM Employee", Long.class);
    long rowsNumber = typedQuery.getSingleResult();
    entityManager.close();
    return rowsNumber;
  }
}
