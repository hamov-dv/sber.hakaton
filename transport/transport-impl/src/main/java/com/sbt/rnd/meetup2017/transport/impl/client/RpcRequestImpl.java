package com.sbt.rnd.meetup2017.transport.impl.client;

import com.sbt.rnd.meetup2017.transport.api.RequestRuntimeException;
import com.sbt.rnd.meetup2017.transport.impl.MessageHandler;
import com.sbt.rnd.meetup2017.transport.impl.MethodInvocation;
import com.sbt.rnd.meetup2017.transport.impl.Rpc;
import com.sbt.rnd.meetup2017.transport.impl.TransportConsumerKafka;
import com.sbt.rnd.meetup2017.transport.message.Message;
import com.sbt.rnd.meetup2017.transport.message.MessageProperties;
import com.sbt.rnd.meetup2017.transport.message.Serializer;
import com.sbt.rnd.meetup2017.transport.producer.TransportProducer;
import com.sbt.rnd.meetup2017.transport.producer.TransportProducerKafka;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.*;

public class RpcRequestImpl implements Rpc, MessageHandler<ConsumerRecord<String, byte[]>> {

    private static final Logger LOGGER = LogManager.getLogger(RpcRequestImpl.class.getName());

    private String groupId = "com.sbt.rnd.meetup2017";
    private String bootstrapServer = "localhost:9092";
    private final Class apiClass;
    private final String nodeId;
    private final String moduleId;

    public RpcRequestImpl(Class apiClass, String nodeId, String moduleId) {
        this.apiClass = apiClass;
        this.nodeId = nodeId;
        this.moduleId = moduleId;
    }

    private Message createRemoteCallMessage(String methodName, Object[] args, Class[] types, String node) {
        MessageProperties properties = new MessageProperties();
        properties.setMethod(methodName);
        properties.setDate(new Date());
        properties.setNodeId(node);
        properties.setApiName(apiClass.getName());
        properties.setModuleId(moduleId);
        properties.setDestination(apiClass.getName());

        MethodInvocation methodInvocation = new MethodInvocation();
        List<Serializable> argList = new ArrayList<>();
        for (Object arg : args) {
            argList.add((Serializable) arg);
        }
        methodInvocation.setArguments(argList);
        methodInvocation.setMethod(methodName);
        List<String> typeList = new ArrayList<>();
        for (Class type : types) {
            typeList.add(type.getName());
        }
        methodInvocation.setArgumentTypes(typeList);
        Message<MethodInvocation> msg = new Message(properties);
        msg.setValue(methodInvocation);

        return msg;
    }

    private String getTopic(Message message) {
        String nodeId = message.getProperties().getNodeId();
        if (nodeId == null) {
            Map<String, List<PartitionInfo>> topics;
            Properties props = new Properties();
            props.put("bootstrap.servers", bootstrapServer);
            props.put("group.id", groupId);
            props.put("key.deserializer", StringDeserializer.class.getName());
            props.put("value.deserializer", ByteArrayDeserializer.class.getName());
            KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(props);
            try {
                topics = consumer.listTopics();
                nodeId = topics.keySet().stream().filter(f -> f.contains(message.getProperties().getDestination() + "_")).findAny().get().split("_")[1];
                message.getProperties().setNodeId(nodeId);
            } finally {
                consumer.close();
            }
        }

        return message.getProperties().getDestinationWithNodeId();
    }

    private String getTopicReply(String topicName) {
        return "reply-"+topicName;
    }

    private <T> T sendMessage(Message message) {
        TransportProducer transportProducer = new TransportProducerKafka(groupId, bootstrapServer);
        String topic = getTopic(message);
        transportProducer.sendMsg(topic, message);
        LOGGER.trace("Send groupId={} bootstrapServer={} topic={} message {}", groupId, bootstrapServer, topic, message.toString());
        MessageHandler handler = this;
        Callable callable = new Callable() {
            @Override
            public Object call() throws Exception {

                TransportConsumerKafka<ConsumerRecord<String, byte[]>> transportConsumer = new TransportConsumerKafka(123, groupId, bootstrapServer, Arrays.asList(getTopicReply(topic)), handler);

                return transportConsumer.getMsgReply();
            }
        };
        ExecutorService threadPool = Executors.newFixedThreadPool(1);
        Future<?> task = threadPool.submit(callable);
        threadPool.shutdown();
        T result = null;
        try {
            result = (T) task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public <T> T callMethod(String methodName, Object[] args, Class[] types, String node) {
        Message msg = createRemoteCallMessage(methodName, args, types, node);

        return sendMessage(msg);
    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public <V> V handle(ConsumerRecord<String, byte[]> record) {

        Message message = Serializer.deserialize(record.value());
        LOGGER.trace("Received answer : " + message.toString());
        if (message.getValue() instanceof RequestRuntimeException) {
            RequestRuntimeException transportRuntimeException = (RequestRuntimeException) message.getValue();
            throw transportRuntimeException;

        }

        return (V) message.getValue();

    }

}
