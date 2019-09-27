package com.sbt.rnd.meetup2017.transport.api.impl;

import com.sbt.rnd.meetup2017.transport.api.RequestApiImpl;
import com.sbt.rnd.meetup2017.transport.api.client.ClientApi;
import com.sbt.rnd.meetup2017.dao.IDao;
import com.sbt.rnd.meetup2017.data.ogm.Address;
import com.sbt.rnd.meetup2017.data.ogm.Client;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestApiImpl
public class ClientApiImpl implements ClientApi {

    @Autowired
    private IDao dao;

    @Override
    public Client create(String name, String inn, Collection<Address> addresses) {
        Client client = new Client(name, inn);
        client.setAddresses(addresses);

        if (dao.save(client))
            return client;
        else
            return null;
    }

    @Override
    public Boolean update(Client client) {
        if (client!=null)
            return dao.save(client,true);
        return false;
    }

    @Override
    public Boolean delete(Long clientId) {
        Client client = dao.findById(Client.class, clientId);
        if (client!=null)
            return dao.remove(client);
        return false;
    }

    @Override
    public List<Client> getClientByInn(String inn) {
        Map<String,Object> parameters=new HashMap<>();
        parameters.put("inn",inn);
        return dao.search("select c from Client c where c.inn=:inn",parameters);
    }

    @Override
    public Client getClientById(Long id){
        Client client=dao.findById(Client.class,id);
        if (client==null)
            throw new RuntimeException("Клиент с id="+id+" не найден в системе");
        return client;

    }
}
