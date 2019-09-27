package com.sbt.rnd.meetup2017.transport.impl.server;

import org.springframework.remoting.support.RemoteInvocationBasedExporter;

import java.util.List;

public class TransportObjectFactoryImpl implements TransportObjectFactory {

    public TransportObjectFactoryImpl() {
    }

    @Override
    public Server createApiServer(String baseApiPackage, List<SpringServerApi.ApiServiceBean> apiServices) {
        Server server = null;
        for (SpringServerApi.ApiServiceBean apiServiceBean : apiServices) {

            RemoteInvocationBasedExporter exporter;

            exporter = new RpcServerImpl(apiServiceBean.getApiClass(), System.getProperty("nodeId"), System.getProperty("moduleId"));

            exporter.setServiceInterface(apiServiceBean.getApiClass());
            exporter.setService(apiServiceBean.getBean());
            server = (Server) exporter;

        }
        return server;
    }
}
