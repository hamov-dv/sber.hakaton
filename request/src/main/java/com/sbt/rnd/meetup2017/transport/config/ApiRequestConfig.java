package com.sbt.rnd.meetup2017.transport.config;

import com.sbt.rnd.meetup2017.transport.api.account.AccountApiRequest;
import com.sbt.rnd.meetup2017.transport.api.client.ClientApiRequest;
import com.sbt.rnd.meetup2017.transport.api.RequestTransportProxyFactory;
import com.sbt.rnd.meetup2017.transport.api.document.DocumentApiRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({TransportConfig.class})
public class ApiRequestConfig {

    @Bean
    ClientApiRequest clientApiRequest(@Qualifier("transportProxyFactory") RequestTransportProxyFactory proxyFactory) {
        ClientApiRequest clientApiRequest = proxyFactory.createRequestTransportProxy(ClientApiRequest.class);
        return clientApiRequest;
    }

    @Bean
    AccountApiRequest accountApiRequest(@Qualifier("transportProxyFactory") RequestTransportProxyFactory proxyFactory) {
        AccountApiRequest accountApiRequest = proxyFactory.createRequestTransportProxy(AccountApiRequest.class);
        return accountApiRequest;
    }

    @Bean
    DocumentApiRequest documentApiRequest(@Qualifier("transportProxyFactory") RequestTransportProxyFactory proxyFactory) {
        DocumentApiRequest documentApiRequest = proxyFactory.createRequestTransportProxy(DocumentApiRequest.class);
        return documentApiRequest;
    }

}
