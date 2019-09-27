package com.sbt.rnd.meetup2017.transport.impl;

public interface MessageHandler<T> {
    <V> V handle(T msg);
}
