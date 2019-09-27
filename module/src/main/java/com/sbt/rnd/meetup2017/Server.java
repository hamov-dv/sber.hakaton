package com.sbt.rnd.meetup2017;

import com.sbt.rnd.meetup2017.data.ogm.Client;
import com.sbt.rnd.meetup2017.transport.api.account.AccountApi;
import com.sbt.rnd.meetup2017.transport.api.client.ClientApi;
import com.sbt.rnd.meetup2017.transport.impl.MethodInvocation;
import com.sbt.rnd.meetup2017.transport.impl.MessageHandler;
import com.sbt.rnd.meetup2017.transport.producer.TransportProducerKafka;
import com.sbt.rnd.meetup2017.transport.impl.TransportListener;
import com.sbt.rnd.meetup2017.transport.message.Message;
import com.sbt.rnd.meetup2017.transport.message.MessageProperties;
import com.sbt.rnd.meetup2017.transport.message.Serializer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Server {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("spring-beans.xml");
        AccountApi accountApi = (AccountApi) context.getBean("accountApi");
        accountApi.getAccountByNumber("21123123");
        ClientApi clientApi = (ClientApi) context.getBean("clientApi");
        Client client = clientApi.create("sdkjfds", "asdasd", null);
        System.out.println(client.getInn());

    }

}
