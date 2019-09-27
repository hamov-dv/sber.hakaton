package com.sbt.rnd.meetup2017.transport.api;

public class RequestTimeoutException extends RuntimeException {

    public RequestTimeoutException() {
        super();
    }

    public RequestTimeoutException(String message) {
        super(message);
    }
}
