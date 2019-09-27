package com.sbt.rnd.meetup2017.transport.api.client;

import com.sbt.rnd.meetup2017.data.ogm.Address;
import com.sbt.rnd.meetup2017.data.ogm.Client;
import com.sbt.rnd.meetup2017.transport.api.Api;

import java.util.Collection;
import java.util.List;

@Api
public interface ClientApi {

    Client create(String name, String inn, Collection<Address> addresses);

    Boolean update(Client client);

    Boolean delete(Long clientId);

    List<Client> getClientByInn(String inn);

    Client getClientById(Long id);

}
