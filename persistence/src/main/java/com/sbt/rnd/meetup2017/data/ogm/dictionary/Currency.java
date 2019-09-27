package com.sbt.rnd.meetup2017.data.ogm.dictionary;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Currency implements Serializable {

    private Long id;

    public Currency() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String code;
    private Integer intCode;
    private String name;
    private Boolean isDefault;

    public Currency(String code, int intCode, String name) {
        this.code = code;
        this.intCode = intCode;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getIntCode() {
        return intCode;
    }

    public void setIntCode(Integer intCode) {
        this.intCode = intCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }
}
