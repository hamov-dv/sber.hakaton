package com.sbt.rnd.meetup2017.transport.impl.server;

import com.sbt.rnd.meetup2017.transport.api.RequestRuntimeException;
import com.sbt.rnd.meetup2017.transport.api.RequestTimeoutException;
import com.sbt.rnd.meetup2017.transport.impl.MessageHandler;
import com.sbt.rnd.meetup2017.transport.impl.MethodInvocation;
import com.sbt.rnd.meetup2017.transport.impl.TransportListener;
import com.sbt.rnd.meetup2017.transport.message.Message;
import com.sbt.rnd.meetup2017.transport.message.MessageProperties;
import com.sbt.rnd.meetup2017.transport.message.Serializer;
import com.sbt.rnd.meetup2017.transport.producer.TransportProducer;
import com.sbt.rnd.meetup2017.transport.producer.TransportProducerKafka;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.remoting.support.RemoteInvocationBasedExporter;
import org.springframework.util.ClassUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class RpcServerImpl extends RemoteInvocationBasedExporter implements MessageHandler<ConsumerRecord<String, byte[]>>, AutoCloseable, Server {

    private static final Logger LOGGER = LogManager.getLogger(RpcServerImpl.class.getName());

    private String groupId = "com.sbt.rnd.meetup2017";
    private String bootstrapServer = "localhost:9092";
    private int numConsumers = 1;
    private final Class<?> apiClass;
    private final String nodeId;
    private final String moduleId;
    private TransportProducer producer;

    public RpcServerImpl(Class<?> apiClass, String nodeId, String moduleId) {
        this.apiClass = apiClass;
        this.nodeId = nodeId;
        this.moduleId = moduleId;
        this.producer = new TransportProducerKafka(groupId, bootstrapServer);
    }

    private Message createReplyMessage(MessageProperties messageProperties, Object result) {

        Message<Object> msg = new Message(messageProperties);

        msg.setValue(result);

        return msg;
    }

    private String getTopic(Message message) {
        return message.getProperties().getDestination();
    }

    private String getTopicReply(Message message) {
        return getTopic(message) + "-reply";
    }

    private ClassLoader getClassLoader() {
        return this.getBeanClassLoader();
    }

    protected Class[] getReceivedParamTypes(List<String> paramTypesList, ClassLoader classLoader)
            throws ClassNotFoundException {
        Class[] paramTypes = new Class[paramTypesList.size()];
        for (int i = 0; i < paramTypesList.size(); i++) {
            paramTypes[i] = ClassUtils.forName(paramTypesList.get(i), classLoader);
        }
        return paramTypes;
    }

    private void reply(MessageProperties messageProperties, Object result) {
        Message message = createReplyMessage(messageProperties, result);
        producer.sendMsg(getTopicReply(message), message);

    }

    private boolean checkTimeOut(Message message) {
        MessageProperties properties = message.getProperties();
        long sendTime = properties.getDate().getTime();
        Long timeOut = properties.getTimeout();
        if (timeOut != null) {
            long expTime = sendTime + timeOut.longValue();
            return System.currentTimeMillis() < expTime;
        }

        return true;

    }

    protected void invoke(Message message)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, ClassNotFoundException {
        if (!(message.getValue() instanceof MethodInvocation)) {
            LOGGER.error("Can't invoke message {}. Message value should be instance of class {}", message, MethodInvocation.class);
            return;
        }
        MethodInvocation methodInvocation = (MethodInvocation) message.getValue();
        Class[] paramTypes = getReceivedParamTypes(methodInvocation.getArgumentTypes(), getClassLoader());
        Method method = getServiceInterface().getMethod(methodInvocation.getMethod(), paramTypes);

        ClassLoader implClassLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader externalClassLoader = getClassLoader();
        ClassLoader executionClassLoader = (externalClassLoader == null ? implClassLoader : externalClassLoader);
        Thread.currentThread().setContextClassLoader(executionClassLoader);
        Object result = null;
        try {
            result = method.invoke(getService(), methodInvocation.getArguments().toArray());
        } finally {
            Thread.currentThread().setContextClassLoader(implClassLoader);
        }

        if (checkTimeOut(message)) {
            reply(message.getProperties(), result);
        } else {
            reply(message.getProperties(), new RequestTimeoutException("Call execution timeout"));
            LOGGER.trace("Reply will not be sent because of call execution timeout. Message headers: {}",
                    message.getProperties());

        }
    }

    @Override
    public void close() throws Exception {
        producer.close();
    }

    @Override
    public <V> V handle(ConsumerRecord<String, byte[]> record) {
        if (record.value() == null) {
            LOGGER.error("Received message value is null");
            return null;
        }
        Message message = null;
        try {
            message = Serializer.deserialize(record.value());
        } catch (ClassCastException ex) {
            LOGGER.error("ClassCastException {}", ex.getMessage());
            reply(message.getProperties(), new RequestRuntimeException(ex.getMessage(),ex.getCause()));
            return null;
        }

        try {
            invoke(message);

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Exception {} {}", e.getMessage(),e.getCause());
            reply(message.getProperties(), new RequestRuntimeException(e.getMessage(),e.getCause()));
       /* } catch (NoSuchMethodException e) {
            e.printStackTrace();
            LOGGER.error("NoSuchMethodException {}", e.getMessage());
            throw new RequestRuntimeException(e.getMessage(), e.getCause());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            LOGGER.error("IllegalAccessException {}", e.getMessage());
            throw new RequestRuntimeException(e.getMessage(), e.getCause());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            LOGGER.error("ClassNotFoundException {}", e.getCause());
            throw new RequestRuntimeException(e.getMessage(), e.getCause());
        } catch (RuntimeException e) {
            LOGGER.error("RuntimeException {}", e.getMessage());
            e.printStackTrace();
            throw new RequestRuntimeException(e.getMessage(), e.getCause());*/
        }

        return null;
    }

    @Override
    public void startServer() {
        List<String> topics = Arrays.asList(apiClass.getName());

      /*  MessageHandler handler = new MessageHandler<ConsumerRecord<String, byte[]>>() {
            @Override
            public <V> V handle(ConsumerRecord<String, byte[]> record) {
                Message<MethodInvocation> message = Serializer.deserialize(record.value());
                System.out.println("message: " + message.toString());
                MethodInvocation methodInvocation = message.getValue();
                System.out.println("methodInvocation: " + methodInvocation.toString());

                MessageProperties properties = new MessageProperties();
                properties.setMethod("test-topic-reply");
                properties.setDate(new Date());
                properties.setNodeId(message.getProperties().getNodeId());
                Message<String> respMsg = new Message(properties);
                //Client client = clientApi.create("clent-test", "123456789", null);
                respMsg.setValue("hello");
                TransportProducerKafka respProd = new TransportProducerKafka(groupId, bootstrapServer);
                respProd.sendMsg(message.getProperties().getDestination() + "-reply", respMsg);
                System.out.println("12334send answer: " + respMsg);

                return null;
            }
        };*/
        new TransportListener(numConsumers, groupId, bootstrapServer, topics).start(this);
        LOGGER.debug("Started Server groupId={} bootstrapServer={} topics={}", groupId, bootstrapServer, topics);
    }

    @Override
    public void stopServer() {
        try {
            producer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
