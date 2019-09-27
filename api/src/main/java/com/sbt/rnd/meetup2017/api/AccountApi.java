package com.sbt.rnd.meetup2017.api;

import com.sbt.rnd.meetup2017.data.ogm.Account;
import com.sbt.rnd.meetup2017.data.ogm.Client;
import com.sbt.rnd.meetup2017.data.ogm.dictionary.Currency;

import java.util.Collection;

public interface AccountApi {

    Account create(Client client,String accountNumber,String name,Currency currency);
    boolean edit(Account account);
    boolean delete(Long accId);
}
