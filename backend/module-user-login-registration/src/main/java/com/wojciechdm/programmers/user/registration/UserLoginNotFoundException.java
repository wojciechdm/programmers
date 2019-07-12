package com.wojciechdm.programmers.user.registration;

public class UserLoginNotFoundException extends RuntimeException {
  UserLoginNotFoundException(String message) {
    super(message);
  }
}
