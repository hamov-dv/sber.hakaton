package com.sbt.rnd.meetup2017.transport.client;

import com.sbt.rnd.meetup2017.data.ogm.Account;
import com.sbt.rnd.meetup2017.data.ogm.Client;
import com.sbt.rnd.meetup2017.data.ogm.Document;
import com.sbt.rnd.meetup2017.transport.api.account.AccountApiRequest;
import com.sbt.rnd.meetup2017.transport.api.client.ClientApiRequest;
import com.sbt.rnd.meetup2017.transport.api.TransportRequest;
import com.sbt.rnd.meetup2017.transport.api.document.DocumentApiRequest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.*;

public class ClientRequest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext("com.sbt.rnd.meetup2017.transport.config");
        ClientApiRequest clientApiRequest = (ClientApiRequest) context.getBean("clientApiRequest");
        TransportRequest<Client> request = clientApiRequest.create("test", "1234567890", null);
        Client client=request.call();
        System.out.println(client.toString());

        AccountApiRequest accountApiRequest = (AccountApiRequest) context.getBean("accountApiRequest");
        TransportRequest<Account> requestAcc = accountApiRequest.create(client.getId(), "408170000000001223", "test",null);
        Account account=requestAcc.call();
        System.out.println(account.toString());

        requestAcc = accountApiRequest.create(client.getId(), "408170000000002343", "test",null);
        Account accountCt=requestAcc.call();
        System.out.println(accountCt.toString());

        DocumentApiRequest documentApiRequest = (DocumentApiRequest) context.getBean("documentApiRequest");
        TransportRequest<Document> requestDoc = documentApiRequest.create(account.getId(),accountCt.getId(), BigDecimal.valueOf(100), "test");
        Document document=requestDoc.call();
        System.out.println(document.toString());

        TransportRequest<Boolean> requestDocWork = documentApiRequest.workOut(document.getId(),new Date());
        Boolean documentWorked=requestDocWork.call();
        System.out.println("workOut="+documentWorked);

        TransportRequest<Boolean> requestAccRes = accountApiRequest.reserveAccount(client.getId(), "408170000000001223",null);
        Boolean accRes=requestAccRes.call();
        System.out.println("reserved="+accRes);

    }
}
