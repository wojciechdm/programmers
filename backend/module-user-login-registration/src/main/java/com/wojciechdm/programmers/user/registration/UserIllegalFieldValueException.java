package com.wojciechdm.programmers.user.registration;

public class UserIllegalFieldValueException extends RuntimeException {

  UserIllegalFieldValueException(String message, Throwable cause) {
    super(message, cause);
  }
}
