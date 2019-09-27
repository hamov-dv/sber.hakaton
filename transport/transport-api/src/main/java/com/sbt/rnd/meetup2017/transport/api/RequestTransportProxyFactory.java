package com.sbt.rnd.meetup2017.transport.api;

public interface RequestTransportProxyFactory {

    <T> T createRequestTransportProxy(Class<T> clazz);

}
