package com.wojciechdm.programmers.controllers.rest.client;

import static java.util.Optional.*;
import static org.springframework.http.HttpStatus.*;

import com.wojciechdm.programmers.company.structure.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
@Slf4j
public class ClientControllerExceptionHandlerAdvice {

  @ExceptionHandler(ClientCodeNameNotUniqueException.class)
  public ResponseEntity<String> handle(ClientCodeNameNotUniqueException exception) {
    return error(CONFLICT, exception);
  }

  @ExceptionHandler(ClientIllegalFieldValueException.class)
  public ResponseEntity<String> handle(ClientIllegalFieldValueException exception) {
    return error(BAD_REQUEST, exception);
  }

  @ExceptionHandler(ClientNotFoundException.class)
  public ResponseEntity<String> handle(ClientNotFoundException exception) {
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
