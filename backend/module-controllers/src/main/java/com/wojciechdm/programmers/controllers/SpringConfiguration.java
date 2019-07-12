package com.wojciechdm.programmers.controllers;

import com.wojciechdm.programmers.company.structure.client.*;
import com.wojciechdm.programmers.company.structure.employee.*;
import com.wojciechdm.programmers.company.structure.project.*;
import com.wojciechdm.programmers.company.structure.projectallocation.*;
import com.wojciechdm.programmers.user.registration.*;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;

import javax.persistence.*;
import javax.sql.DataSource;
import java.util.Map;

@Configuration
public class SpringConfiguration {

  @Bean
  @ConfigurationProperties("app.datasource")
  DataSource dataSource() {
    return DataSourceBuilder.create().type(HikariDataSource.class).build();
  }

  @Bean
  EntityManagerFactory entityManagerFactory() {
    return Persistence.createEntityManagerFactory(
        "persistence", Map.of("javax.persistence.nonJtaDataSource", dataSource()));
  }

  @Bean
  ClientRepository clientRepository() {
    return new ClientRepository(dataSource());
  }

  @Bean
  public ClientServiceFacade clientServiceFacade() {
    return new ClientServiceFacade(clientRepository());
  }

  @Bean
  UserRepository userRepository() {
    return new UserRepository(dataSource());
  }

  @Bean
  public UserServiceFacade userServiceFacade() {
    return new UserServiceFacade(userRepository());
  }

  @Bean
  UserLoginRepository userLoginRepository() {
    return new UserLoginRepository(dataSource());
  }

  @Bean
  UserLoginServiceFacade userLoginServiceFacade() {
    return new UserLoginServiceFacade(userLoginRepository());
  }

  @Bean
  EmployeeRepository employeeRepository() {
    return new EmployeeRepository(dataSource());
  }

  @Bean
  public EmployeeServiceFacade employeeServiceFacade() {
    return new EmployeeServiceFacade(employeeRepository());
  }

  @Bean
  ProjectRepository projectRepository() {
    return new ProjectRepository(dataSource());
  }

  @Bean
  public ProjectServiceFacade projectServiceFacade() {
    return new ProjectServiceFacade(projectRepository());
  }

  @Bean
  ProjectAllocationJpaRepository projectAllocationJpaRepository() {
    return new ProjectAllocationJpaRepository(entityManagerFactory());
  }

  @Bean
  public ProjectAllocationServiceFacade projectAllocationServiceFacade() {
    return new ProjectAllocationServiceFacade(
        projectAllocationJpaRepository(), employeeServiceFacade(), projectServiceFacade());
  }
}
