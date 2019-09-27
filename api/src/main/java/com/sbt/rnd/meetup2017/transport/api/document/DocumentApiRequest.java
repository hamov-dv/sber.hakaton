package com.sbt.rnd.meetup2017.transport.api.document;

import com.sbt.rnd.meetup2017.data.ogm.Document;
import com.sbt.rnd.meetup2017.transport.api.ApiClass;
import com.sbt.rnd.meetup2017.transport.api.TransportRequest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@ApiClass(api=DocumentApi.class)
public interface DocumentApiRequest {

    TransportRequest<Document> create(Long debetAccount, Long creditAccount, BigDecimal sumDoc, String aim);

    TransportRequest<Boolean> update(Document document);

    TransportRequest<Boolean> delete(Long docId);

    TransportRequest<Boolean> workOut(Long docId, Date dateWork);

    TransportRequest<List<Document>> getDocumentsByClient(Long clientId, DocumentFilter filter);

    TransportRequest<Document> getDocumentById(Long id);

}
