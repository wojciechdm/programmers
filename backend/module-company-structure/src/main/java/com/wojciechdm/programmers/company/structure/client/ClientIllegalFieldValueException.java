package com.wojciechdm.programmers.company.structure.client;

public class ClientIllegalFieldValueException extends RuntimeException {

  ClientIllegalFieldValueException(String message, Throwable cause) {
    super(message, cause);
  }
}
