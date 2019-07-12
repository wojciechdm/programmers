package com.wojciechdm.programmers.user.registration;

public class UsernameNotUniqueException extends RuntimeException {
  UsernameNotUniqueException(String message) {
    super(message);
  }
}
