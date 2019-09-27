package com.sbt.rnd.meetup2017.transport.api.document;

import java.io.Serializable;

public enum DocumentAccTypeFilter implements Serializable {

    BY_DT_CT(0, "По счету ДТ и КТ"),
    BY_DT(1, "По счету ДТ"),
    BY_KT(2, "По счету КТ");

    private final int code;
    private final String name;


    private DocumentAccTypeFilter(int code, String name) {
        this.code = code;
        this.name = name;
    }


    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "DocumentState{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
