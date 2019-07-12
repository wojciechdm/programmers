package com.wojciechdm.programmers.controllers.rest.projectAllocation;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.*;

import com.wojciechdm.programmers.company.structure.projectallocation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ProjectAllocationControllerExceptionHandlerAdvice {

  @ExceptionHandler(ProjectAllocationIllegalFieldValueException.class)
  public ResponseEntity<String> handle(ProjectAllocationIllegalFieldValueException exception) {
    return error(BAD_REQUEST, exception);
  }

  @ExceptionHandler(ProjectAllocationValidationException.class)
  public ResponseEntity<String> handle(ProjectAllocationValidationException exception) {
    return error(CONFLICT, exception);
  }

  @ExceptionHandler(ProjectAllocationNotFoundException.class)
  public ResponseEntity<String> handle(ProjectAllocationNotFoundException exception) {
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
