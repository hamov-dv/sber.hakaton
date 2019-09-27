package com.sbt.rnd.meetup2017.transport.api.account;

import com.sbt.rnd.meetup2017.data.ogm.Account;
import com.sbt.rnd.meetup2017.transport.api.ApiClass;
import com.sbt.rnd.meetup2017.transport.api.TransportRequest;

import java.util.List;

@ApiClass(api=AccountApi.class)
public interface AccountApiRequest {

    TransportRequest<Account> create(Long clientId, String accountNumber, String name, Integer currencyIntCode);

    TransportRequest<Boolean> update(Account account);

    TransportRequest<Boolean> delete(Long accId);

    TransportRequest<Boolean> reserveAccount(Long clientId, String accountNumber, Integer currencyIntCode);

    TransportRequest<Boolean> openAccount(String accountNumber);

    TransportRequest<List<Account>> getAccountsByClient(Long clientId);

    TransportRequest<Account> getAccountById(Long id);

    TransportRequest<Account> getAccountByNumber(String accountNumber);

    TransportRequest<Boolean> accountIsOpen(Account account);
}
