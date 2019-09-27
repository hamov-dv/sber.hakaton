package com.sbt.rnd.meetup2017.transport.api.account;

import com.sbt.rnd.meetup2017.data.ogm.Account;
import com.sbt.rnd.meetup2017.transport.api.Api;

import java.util.List;

@Api
public interface AccountApi {

    Account create(Long clientId, String accountNumber, String name, Integer currencyIntCode);

    Boolean update(Account account);

    Boolean delete(Long accId);

    Boolean reserveAccount(Long clientId,String accountNumber,Integer currencyIntCode);

    Boolean openAccount(String accountNumber);

    List<Account> getAccountsByClient(Long clientId);

    Account getAccountById(Long id);

    Account getAccountByNumber(String accountNumber);

    Boolean accountIsOpen(Account account);
}
