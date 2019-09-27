package com.sbt.rnd.meetup2017.transport.producer;

import com.sbt.rnd.meetup2017.transport.message.Serializer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class TransportProducerKafka implements TransportProducer{

    private Producer<String, byte[]> producer;

    public TransportProducerKafka(String groupId, String bootstrapServer) {

        Properties props = new Properties() {{
            put("bootstrap.servers", bootstrapServer);
            put("acks", "all");
            put("retries", 1);
            put("buffer.memory", 33554432);
            put("batch.size", 16384);
            put("linger.ms", 1);
            put("client.id", groupId);
            put("key.serializer", org.apache.kafka.common.serialization.StringSerializer.class.getName());
            put("value.serializer", org.apache.kafka.common.serialization.ByteArraySerializer.class.getName());
        }};
        producer = new KafkaProducer<String, byte[]>(props);
    }

    public <T> void sendMsg(String topicName,T msg) {

        sendMsg(topicName,msg,null);
    }

    public <T> void sendMsg(String topicName,T msg,Callback callback) {

        producer.send(new ProducerRecord<String, byte[]>(topicName, String.valueOf(msg.hashCode()) ,Serializer.serialize(msg)),callback);
        //producer.close();
    }

    @Override
    public void close() throws Exception {
        producer.close();
    }
}
