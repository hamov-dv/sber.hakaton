package com.sbt.rnd.meetup2017.transport.api.impl;

import com.sbt.rnd.meetup2017.dao.IDao;
import com.sbt.rnd.meetup2017.data.ogm.dictionary.Currency;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Scope("singleton")
public class CurrencyUtils {

    private IDao dao;

    private static CurrencyUtils currencyUtils;

    public synchronized static CurrencyUtils getInstance(IDao dao) {
        if (currencyUtils == null)
            currencyUtils = new CurrencyUtils(dao);

        return currencyUtils;

    }

    private CurrencyUtils(IDao dao) {
        this.dao = dao;
        if (getDefault() == null) {
            Currency currency = new Currency("RUB", 810, "Российский рубль");
            currency.setDefault(true);
            dao.save(currency);
        }

    }

    public Currency getByIntCode(Integer currencyIntCode) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("intCode", currencyIntCode);
        List<Currency> currencyList = dao.search("select c from Currency c where c.intCode=:intCode",parameters);
        if (currencyList.size() == 0)
            throw new RuntimeException("Валюта с IntCode=" + currencyIntCode + " не найдена в системе");
        else if (currencyList.size() > 1)
            throw new RuntimeException("Найдено несколько валют с IntCode=" + currencyIntCode);

        return currencyList.get(0);

    }

    public Currency getDefault() {

        List<Currency> currencyList = dao.search("select c from Currency c where c.default=true");
        if (currencyList.size() == 0)
            /*throw new RuntimeException("В системе не найдена валюта по умолчанию");*/
            return null;
        else if (currencyList.size() > 1)
            throw new RuntimeException("Найдено несколько валют по умолчанию");

        return currencyList.get(0);

    }
}
