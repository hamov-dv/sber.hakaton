package com.sbt.rnd.meetup2017.transport.api.document;

import com.sbt.rnd.meetup2017.data.ogm.Document;
import com.sbt.rnd.meetup2017.transport.api.Api;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Api
public interface DocumentApi {

    Document create(Long debetAccount, Long creditAccount, BigDecimal sumDoc, String aim);

    Boolean update(Document document);

    Boolean delete(Long docId);

    Boolean workOut(Long docId, Date dateWork);

    List<Document> getDocumentsByClient(Long clientId,DocumentFilter filter);

    Document getDocumentById(Long id);

}
