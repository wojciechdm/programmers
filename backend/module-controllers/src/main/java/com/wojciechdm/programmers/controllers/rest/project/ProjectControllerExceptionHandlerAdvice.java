package com.wojciechdm.programmers.controllers.rest.project;

import com.wojciechdm.programmers.company.structure.project.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@Slf4j
public class ProjectControllerExceptionHandlerAdvice {

  @ExceptionHandler(ProjectCodeNameNotUniqueForClientException.class)
  public ResponseEntity<String> handle(ProjectCodeNameNotUniqueForClientException exception) {
    return error(CONFLICT, exception);
  }

  @ExceptionHandler(ProjectIllegalFieldValueException.class)
  public ResponseEntity<String> handle(ProjectIllegalFieldValueException exception) {
    return error(BAD_REQUEST, exception);
  }

  @ExceptionHandler(ProjectNotFoundException.class)
  public ResponseEntity<String> handle(ProjectNotFoundException exception) {
    return error(NOT_FOUND, exception);
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<String> handle(IllegalStateException exception) {
    return error(SERVICE_UNAVAILABLE, exception);
  }

  private ResponseEntity<String> error(HttpStatus httpStatus, Exception exception) {
    log.error("Exception: ", exception);

    return ResponseEntity.status(httpStatus)
        .body(
            exception.getMessage()
                + ofNullable(exception.getCause()).map(e -> ": " + e.getMessage()).orElse(""));
  }
}
