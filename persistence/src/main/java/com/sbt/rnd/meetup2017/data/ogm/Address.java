package com.sbt.rnd.meetup2017.data.ogm;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class Address implements Serializable {

    private String street;
    private int zip;

    public Address() {
    }

    public Address(String street, int zip) {
        this.street = street;
        this.zip = zip;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }
}
