package com.sbt.rnd.meetup2017.transport.producer;

import org.apache.kafka.clients.producer.Callback;

public interface TransportProducer extends AutoCloseable {

    <T>  void sendMsg(String topicName,T msg,Callback callback);

    <T>  void sendMsg(String topicName,T msg);
}
