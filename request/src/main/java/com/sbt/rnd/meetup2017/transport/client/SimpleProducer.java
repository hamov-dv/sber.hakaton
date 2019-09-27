package com.sbt.rnd.meetup2017.transport.client;

import com.sbt.rnd.meetup2017.data.ogm.Client;
import com.sbt.rnd.meetup2017.transport.impl.TransportConsumerKafka;
import com.sbt.rnd.meetup2017.transport.impl.MessageHandler;
import com.sbt.rnd.meetup2017.transport.producer.TransportProducerKafka;
import com.sbt.rnd.meetup2017.transport.message.Message;
import com.sbt.rnd.meetup2017.transport.message.MessageProperties;
import com.sbt.rnd.meetup2017.transport.message.Serializer;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.*;

public class SimpleProducer {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        String topic = "com.sbt.rnd.meetup2017.transport.api.client.ClientApiRequest";
        String groupId = "com.sbt.rnd.meetup2017";
        String bootstrapServer="localhost:9092";
        MessageProperties properties = new MessageProperties();
        properties.setMethod("test-topic");
        properties.setDate(new Date());
        properties.setNodeId(System.getProperty("nodeId"));

        Message<String> msg = new Message(properties);
        msg.setValue("this is message SimpleProducer");

        TransportProducerKafka transportProducer = new TransportProducerKafka(groupId, bootstrapServer);

        transportProducer.sendMsg(topic,msg);
        System.out.println("message sent.");
        Callable callable = new Callable() {
            @Override
            public Object call() throws Exception {
                String result;
                TransportConsumerKafka transportConsumer = new TransportConsumerKafka(134, groupId, bootstrapServer, Arrays.asList(topic + "-reply"), new MessageHandler<ConsumerRecord<String, byte[]>>() {

                    @Override
                    public <V> V handle(ConsumerRecord<String, byte[]> record) {
                        Client msg = Serializer.deserialize(record.value());
                        System.out.println("Received answer msg: " + msg.toString());

                        return (V) msg.toString();
                    }
                });

                return transportConsumer.getMsgReply();
            }
        };
        ExecutorService threadPool = Executors.newFixedThreadPool(1);
        Future<?> task = threadPool.submit(callable);
        threadPool.shutdown();

        System.out.println("result = " + task.get());


    }
}
