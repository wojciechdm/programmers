package com.wojciechdm.programmers.company.structure.client;

public class ClientCodeNameNotUniqueException extends RuntimeException {

    ClientCodeNameNotUniqueException(String message) {
        super(message);
    }
}
