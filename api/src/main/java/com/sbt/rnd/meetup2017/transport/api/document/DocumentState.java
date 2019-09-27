package com.sbt.rnd.meetup2017.transport.api.document;

import com.sbt.rnd.meetup2017.transport.api.StateHolder;

import java.io.Serializable;

public enum DocumentState implements StateHolder,Serializable{

    CREATED(    0,  "Создан",               "Документ создан" ),
    CHECKOUT(   1,  "На проверке",          "Документ на проверке" ),
    FORSIG(     2,  "Ожидает подписи",      "Документ ожидает подписи" ),
    PARTSIG(    3,  "Частично подписан",    "Документ частично подписан" ),
    ACCEPTED(   4,  "Принят в обработку",   "Документ принят в обработку" ),
    REJECT(     5,  "Отклонен",             "Документ отклонен" ),
    CANCELLED(  6,  "Отменен",              "Документ отменен" ),
    PASSED(     7,  "Исполняется",          "Документ исполняется" ),
    DONE(       8,  "Исполнен",             "Документ исполнен" ),
    ERROR(      9,  "Ошибка",               "Ошибка документа" );

    private final int code;
    private final String name;
    private final String descr;

    private DocumentState(int code, String name, String descr) {
        this.code = code;
        this.name = name;
        this.descr = descr;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription()
    {
        return descr;
    }

    @Override
    public String toString() {
        return "DocumentState{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", descr='" + descr + '\'' +
                '}';
    }
}
