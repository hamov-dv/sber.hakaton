package com.sbt.rnd.meetup2017.transport.api.client;

import com.sbt.rnd.meetup2017.data.ogm.Address;
import com.sbt.rnd.meetup2017.data.ogm.Client;
import com.sbt.rnd.meetup2017.transport.api.ApiClass;
import com.sbt.rnd.meetup2017.transport.api.TransportRequest;

import java.util.Collection;
import java.util.List;

@ApiClass(api=ClientApi.class)
public interface ClientApiRequest {

    TransportRequest<Client> create(String name, String inn, Collection<Address> addresses);

    TransportRequest<Boolean> update(Client client);

    TransportRequest<Boolean> delete(Long clientId);

    TransportRequest<List<Client>> getClientByInn(String inn);

    TransportRequest<Client> getClientById(Long id);

}
