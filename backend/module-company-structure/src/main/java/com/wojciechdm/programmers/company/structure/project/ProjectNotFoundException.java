package com.wojciechdm.programmers.company.structure.project;

public class ProjectNotFoundException extends RuntimeException {

  ProjectNotFoundException(String message) {
    super(message);
  }
}
