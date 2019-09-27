package com.sbt.rnd.meetup2017.ignite;

import javax.persistence.Persistence;

import static com.sbt.rnd.meetup2017.Environment.PERSISTENCE_UNIT_NAME;

public class RunModule {
    public static void main(String[] args) {
        Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }
}
