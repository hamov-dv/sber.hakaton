package com.sbt.rnd.meetup2017.transport.api.impl;

import com.sbt.rnd.meetup2017.transport.api.RequestApiImpl;
import com.sbt.rnd.meetup2017.transport.api.account.AccountApi;
import com.sbt.rnd.meetup2017.transport.api.document.DocumentApi;
import com.sbt.rnd.meetup2017.transport.api.document.DocumentFilter;
import com.sbt.rnd.meetup2017.dao.IDao;
import com.sbt.rnd.meetup2017.data.ogm.Account;
import com.sbt.rnd.meetup2017.data.ogm.Document;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestApiImpl
public class DocumentApiImpl implements DocumentApi {

    @Autowired
    private IDao dao;

    @Autowired
    AccountApi accountApi;

    @Override
    public Document create(Long debetAccount, Long creditAccount, BigDecimal sumDoc, String aim) {
        Account accountDt = accountApi.getAccountById(debetAccount);
        Account accountCt = accountApi.getAccountById(creditAccount);
        Document document = new Document(accountDt, accountCt, sumDoc, new Date(), aim);
        if (dao.save(document))
            return document;

        return null;
    }

    @Override
    public Boolean update(Document document) {
        if (document != null)
            return dao.save(document,true);
        return false;
    }

    @Override
    public Boolean delete(Long docId) {
        Document document = dao.findById(Document.class, docId);
        if (document != null)
            return dao.remove(document);
        return false;
    }

    @Override
    public Boolean workOut(Long docId, Date dateWork) {
        return false;
    }

    @Override
    public List<Document> getDocumentsByClient(Long clientId, DocumentFilter filter) {
        Map<String, Object> parameters = new HashMap<>();
        String query = "select d from Document d where";
        parameters.put("clientId", clientId);
        switch (filter.getByAcc()) {
            case BY_DT_CT:
                query += " (d.creditAccount.clientId=:clientId or d.debetAccount.clientId=:clientId)";
                break;
            case BY_KT:
                query += " d.creditAccount.clientId=:clientId";
                break;
            default:
                query += " d.debetAccount.clientId=:clientId";
        }

        if (filter.getDateDoc() != null) {
            parameters.put("dateDoc", filter.getDateDoc());
            query += " and d.dateDoc=:dateDoc";
        }

        if (filter.getState() != null) {
            parameters.put("state", filter.getState().getCode());
            query += " and d.state=:state";
        }

        //return dao.search("select d from Document d where d.debetAccount.clientId=:clientId", parameters);

        return dao.search(query, parameters);
    }

    @Override
    public Document getDocumentById(Long id) {
        Document document = dao.findById(Document.class, id);
        if (document == null)
            throw new RuntimeException("Документ с id=" + id + " не найден в системе");
        return document;
    }

}
