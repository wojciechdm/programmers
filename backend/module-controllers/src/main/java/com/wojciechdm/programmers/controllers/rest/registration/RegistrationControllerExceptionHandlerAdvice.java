package com.wojciechdm.programmers.controllers.rest.registration;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.*;

import com.wojciechdm.programmers.user.registration.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class RegistrationControllerExceptionHandlerAdvice {

  @ExceptionHandler(UsernameNotUniqueException.class)
  public ResponseEntity<String> handle(UsernameNotUniqueException exception) {
    return error(CONFLICT, exception);
  }

  @ExceptionHandler(UserIllegalFieldValueException.class)
  public ResponseEntity<String> handle(UserIllegalFieldValueException exception) {
    return error(BAD_REQUEST, exception);
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
