package com.wojciechdm.programmers.company.structure.projectallocation;

import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.*;
import java.util.*;

import static java.util.Objects.*;

@AllArgsConstructor
public class ProjectAllocationJpaRepository implements ProjectAllocationDao {

  private EntityManagerFactory entityManagerFactory;

  @Override
  public ProjectAllocation save(ProjectAllocation projectAllocation) {
    try {
      EntityManager entityManager = entityManagerFactory.createEntityManager();
      entityManager.getTransaction().begin();
      ProjectAllocation projectAllocationSaved = entityManager.merge(projectAllocation);
      entityManager.getTransaction().commit();
      entityManager.close();
      return projectAllocationSaved;
    } catch (PersistenceException e) {
      if ((e.getCause() instanceof ConstraintViolationException)) {
        throw new ProjectAllocationIllegalFieldValueException(
            "Invalid value of project allocation field during save", e);
      } else {
        throw new IllegalStateException(e);
      }
    }
  }

  @Override
  public ProjectAllocation update(ProjectAllocation projectAllocation) {
    try {
      EntityManager entityManager = entityManagerFactory.createEntityManager();
      entityManager.getTransaction().begin();
      ProjectAllocation projectAllocationSaved = entityManager.merge(projectAllocation);
      entityManager.getTransaction().commit();
      entityManager.close();
      return projectAllocationSaved;
    } catch (PersistenceException e) {
      if ((e.getCause() instanceof ConstraintViolationException)) {
        throw new ProjectAllocationIllegalFieldValueException(
            "Invalid value of project allocation field during update", e);
      } else {
        throw new IllegalStateException(e);
      }
    }
  }

  @Override
  public boolean delete(long id) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    Optional.ofNullable(entityManager.find(ProjectAllocation.class, id))
        .ifPresent(entityManager::remove);
    ProjectAllocation projectAllocation = entityManager.find(ProjectAllocation.class, id);
    entityManager.getTransaction().commit();
    entityManager.close();
    return isNull(projectAllocation);
  }

  @Override
  public Optional<ProjectAllocation> fetch(long id) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    Optional<ProjectAllocation> projectAllocation =
        Optional.ofNullable(entityManager.find(ProjectAllocation.class, id));
    entityManager.close();
    return projectAllocation;
  }

  @Override
  public List<ProjectAllocation> fetchByEmployeeId(long id) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    TypedQuery<ProjectAllocation> typedQuery =
        entityManager.createQuery(
            "SELECT p FROM ProjectAllocation AS p WHERE p.employeeId = :employeeId",
            ProjectAllocation.class);
    typedQuery.setParameter("employeeId", id);
    List<ProjectAllocation> projectAllocations =
        Optional.ofNullable(typedQuery.getResultList()).orElse(Collections.emptyList());
    entityManager.close();
    return projectAllocations;
  }

  @Override
  public List<ProjectAllocation> fetchByProjectId(long id) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    TypedQuery<ProjectAllocation> typedQuery =
        entityManager.createQuery(
            "SELECT p FROM ProjectAllocation AS p WHERE p.projectId = :projectId",
            ProjectAllocation.class);
    typedQuery.setParameter("projectId", id);
    List<ProjectAllocation> projectAllocations =
        Optional.ofNullable(typedQuery.getResultList()).orElse(Collections.emptyList());
    entityManager.close();
    return projectAllocations;
  }

  @Override
  public List<ProjectAllocation> fetchAll(
      int page, int limit, ProjectAllocationSortProperty sortProperty, boolean sortDesc) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    TypedQuery<ProjectAllocation> typedQuery =
        entityManager.createQuery(
            "SELECT p FROM ProjectAllocation AS p ORDER BY "
                + sortProperty.getColumnName()
                + (sortDesc ? " DESC" : ""),
            ProjectAllocation.class);
    typedQuery.setFirstResult((page - 1) * limit);
    typedQuery.setMaxResults(limit);
    List<ProjectAllocation> projectsAllocations =
        Optional.ofNullable(typedQuery.getResultList()).orElse(Collections.emptyList());
    entityManager.close();
    return projectsAllocations;
  }

  @Override
  public boolean exists(long id) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    ProjectAllocation projectAllocation = entityManager.find(ProjectAllocation.class, id);
    entityManager.close();
    return nonNull(projectAllocation);
  }

  @Override
  public long count() {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    TypedQuery<Long> typedQuery =
        entityManager.createQuery("SELECT COUNT(*) FROM ProjectAllocation", Long.class);
    long rowsNumber = typedQuery.getSingleResult();
    entityManager.close();
    return rowsNumber;
  }
}
