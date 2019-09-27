package com.sbt.rnd.meetup2017.transport.impl;

import com.sbt.rnd.meetup2017.transport.api.ApiClass;
import com.sbt.rnd.meetup2017.transport.api.RequestTransportProxyFactory;
import com.sbt.rnd.meetup2017.transport.impl.client.RequestInterceptor;
import com.sbt.rnd.meetup2017.transport.impl.client.RpcRequestImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.aop.framework.ProxyFactory;

public class RequestTransportProxyFactoryImpl implements RequestTransportProxyFactory {

    private static final Logger LOGGER = LogManager.getLogger(RequestTransportProxyFactoryImpl.class.getName());

    @Override
    public <T> T createRequestTransportProxy(Class<T> clazz) {

        RequestInterceptor requestInterceptor = new RequestInterceptor();
        if (!clazz.isAnnotationPresent(ApiClass.class) || clazz.getAnnotation(ApiClass.class).api() == null) {
            LOGGER.error("Not found annotation {} in interface {}", ApiClass.class, clazz);

            return null;
        }

        Rpc rpc = new RpcRequestImpl(clazz.getAnnotation(ApiClass.class).api(), System.getProperty("nodeId"), System.getProperty("moduleId"));
        requestInterceptor.setRpc(rpc);

        ProxyFactory proxyFactory = new ProxyFactory(clazz, requestInterceptor);
        //proxyFactory.setAopProxyFactory(new TransportAopProxyFactory());
        return (T) proxyFactory.getProxy();
    }
}
