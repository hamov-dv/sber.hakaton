package com.sbt.rnd.meetup2017.transport.impl;

import java.io.Serializable;
import java.util.List;

public class MethodInvocation implements Serializable{

    private String method;
    private List<String> argumentTypes;
    private List<Serializable> arguments;

    public MethodInvocation() {
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<String> getArgumentTypes() {
        return argumentTypes;
    }

    public void setArgumentTypes(List<String> argumentTypes) {
        this.argumentTypes = argumentTypes;
    }

    public List<Serializable> getArguments() {
        return arguments;
    }

    public void setArguments(List<Serializable> arguments) {
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return "MethodInvocation{" +
                "method='" + method + '\'' +
                ", argumentTypes=" + argumentTypes +
                ", arguments=" + arguments +
                '}';
    }
}
