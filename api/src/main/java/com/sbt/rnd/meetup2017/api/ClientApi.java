package com.sbt.rnd.meetup2017.api;

import com.sbt.rnd.meetup2017.data.ogm.Address;
import com.sbt.rnd.meetup2017.data.ogm.Client;

import java.util.Collection;

public interface ClientApi {

    Client create(String name, String inn, Collection<Address> addresses);
    boolean edit(Client client);
    boolean delete(Long clientId);
}
