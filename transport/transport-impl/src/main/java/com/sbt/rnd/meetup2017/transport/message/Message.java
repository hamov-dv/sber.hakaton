package com.sbt.rnd.meetup2017.transport.message;

import java.io.Serializable;
import java.util.Arrays;

public class Message<T> implements Serializable {

    private final MessageProperties properties;

    private T value;

    public Message(MessageProperties properties) {
        this.properties = properties;
    }

    public MessageProperties getProperties() {
        return properties;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Message{" +
                "properties=" + properties +
                ", value=" + value +
                '}';
    }

    @Override
    public int hashCode() {
        int result = properties != null ? properties.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
