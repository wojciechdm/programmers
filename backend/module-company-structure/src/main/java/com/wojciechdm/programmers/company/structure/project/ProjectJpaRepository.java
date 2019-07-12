package com.wojciechdm.programmers.company.structure.project;

import static java.util.Objects.*;

import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.*;
import java.util.*;

@AllArgsConstructor
public class ProjectJpaRepository implements ProjectDao {

  private EntityManagerFactory entityManagerFactory;

  @Override
  public Project save(Project project) {
    try {
      EntityManager entityManager = entityManagerFactory.createEntityManager();
      entityManager.getTransaction().begin();

      TypedQuery<Long> typedQuery =
          entityManager.createQuery(
              "SELECT p.id FROM Project p WHERE p.codeName = :codeName AND p.clientId = :clientId",
              Long.class);
      typedQuery.setParameter("codeName", project.getCodeName());
      typedQuery.setParameter("clientId", project.getClientId());

      if (!typedQuery.getResultList().isEmpty()) {
        throw new ProjectCodeNameNotUniqueForClientException(
            "Not unique code name for client during save");
      }

      Project projectSaved = entityManager.merge(project);
      entityManager.getTransaction().commit();
      entityManager.close();
      return projectSaved;
    } catch (PersistenceException e) {
      if ((e.getCause() instanceof ConstraintViolationException)) {
        throw new ProjectIllegalFieldValueException(
            "Invalid value of project field during save", e);
      } else {
        throw new IllegalStateException(e);
      }
    }
  }

  @Override
  public Project update(Project project) {
    try {
      EntityManager entityManager = entityManagerFactory.createEntityManager();
      entityManager.getTransaction().begin();

      TypedQuery<Long> typedQuery =
          entityManager.createQuery(
              "SELECT p.id FROM Project p WHERE p.codeName = :codeName AND p.clientId = :clientId",
              Long.class);
      typedQuery.setParameter("codeName", project.getCodeName());
      typedQuery.setParameter("clientId", project.getClientId());

      if (!typedQuery.getResultList().isEmpty()
          && !project.getId().equals(typedQuery.getSingleResult())) {
        throw new ProjectCodeNameNotUniqueForClientException(
            "Not unique code name for client during update");
      }

      project.setCreateDate(entityManager.find(Project.class, project.getId()).getCreateDate());
      Project projectSaved = entityManager.merge(project);
      entityManager.getTransaction().commit();
      entityManager.close();
      return projectSaved;
    } catch (PersistenceException e) {
      if ((e.getCause() instanceof ConstraintViolationException)) {
        throw new ProjectIllegalFieldValueException(
            "Invalid value of project field during update", e);
      } else {
        throw new IllegalStateException(e);
      }
    }
  }

  @Override
  public boolean delete(long id) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    Optional.ofNullable(entityManager.find(Project.class, id)).ifPresent(entityManager::remove);
    Project project = entityManager.find(Project.class, id);
    entityManager.getTransaction().commit();
    entityManager.close();
    return isNull(project);
  }

  @Override
  public Optional<Project> fetch(long id) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    Optional<Project> project = Optional.ofNullable(entityManager.find(Project.class, id));
    entityManager.close();
    return project;
  }

  @Override
  public List<Project> fetchByClientId(long id) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    TypedQuery<Project> typedQuery =
        entityManager.createQuery(
            "SELECT p FROM Project AS p WHERE p.clientId = :clientId", Project.class);
    typedQuery.setParameter("clientId", id);
    List<Project> projects =
        Optional.ofNullable(typedQuery.getResultList()).orElse(Collections.emptyList());
    entityManager.close();
    return projects;
  }

  @Override
  public List<Project> fetchAll(
      int page, int limit, ProjectSortProperty sortProperty, boolean sortDesc) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    TypedQuery<Project> typedQuery =
        entityManager.createQuery(
            "SELECT p FROM Project AS p ORDER BY "
                + sortProperty.getColumnName()
                + (sortDesc ? " DESC" : ""),
            Project.class);
    typedQuery.setFirstResult((page - 1) * limit);
    typedQuery.setMaxResults(limit);
    List<Project> projects =
        Optional.ofNullable(typedQuery.getResultList()).orElse(Collections.emptyList());
    entityManager.close();
    return projects;
  }

  @Override
  public boolean exists(long id) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    Project project = entityManager.find(Project.class, id);
    entityManager.close();
    return nonNull(project);
  }

  @Override
  public long count() {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    TypedQuery<Long> typedQuery =
        entityManager.createQuery("SELECT COUNT(*) FROM Project", Long.class);
    long rowsNumber = typedQuery.getSingleResult();
    entityManager.close();
    return rowsNumber;
  }
}
