package com.wojciechdm.programmers.company.structure.client;

public class ClientNotFoundException extends RuntimeException {

    ClientNotFoundException(String message){
        super(message);
    }
}
