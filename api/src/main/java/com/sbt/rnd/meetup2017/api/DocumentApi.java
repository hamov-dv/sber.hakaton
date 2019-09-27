package com.sbt.rnd.meetup2017.api;

import com.sbt.rnd.meetup2017.data.ogm.Account;
import com.sbt.rnd.meetup2017.data.ogm.Client;
import com.sbt.rnd.meetup2017.data.ogm.Document;
import com.sbt.rnd.meetup2017.data.ogm.dictionary.Currency;

import java.math.BigDecimal;

public interface DocumentApi {

    Document create(Account debetAccount, Account creditAccount, BigDecimal sumDoc, String aim);
    boolean edit(Document document);
    boolean delete(Long docId);
}
