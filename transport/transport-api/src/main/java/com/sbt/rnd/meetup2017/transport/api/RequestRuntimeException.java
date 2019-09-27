package com.sbt.rnd.meetup2017.transport.api;

public class RequestRuntimeException extends RuntimeException {

    public RequestRuntimeException() {
        super();
    }

    public RequestRuntimeException(String message) {
        super(message);
    }

    public RequestRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestRuntimeException(Throwable cause) {
        super(cause);
    }
}
