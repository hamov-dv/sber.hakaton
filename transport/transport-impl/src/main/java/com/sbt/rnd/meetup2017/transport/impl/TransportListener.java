package com.sbt.rnd.meetup2017.transport.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TransportListener {

    private int numConsumers = 1;
    private String groupId;
    private List<String> topics;
    private String bootstrapServer;

    public TransportListener(int numConsumers, String groupId, String bootstrapServer, List<String> topics) {
        this.numConsumers = numConsumers;
        this.groupId = groupId;
        this.topics = topics;
        this.bootstrapServer = bootstrapServer;

    }

    public <T> void start(MessageHandler<T> msgHandler) {

        ExecutorService executor = Executors.newFixedThreadPool(numConsumers);
        final List<TransportConsumerKafka> transportConsumers = new ArrayList<>();

        for (int i = 0; i < numConsumers; i++) {
            TransportConsumerKafka transportConsumer = new TransportConsumerKafka(i, groupId, bootstrapServer, topics, msgHandler);
            transportConsumers.add(transportConsumer);
            executor.submit(transportConsumer);
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                for (TransportConsumerKafka transportConsumer : transportConsumers) {
                    transportConsumer.shutdown();
                }
                executor.shutdown();
                try {
                    executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
