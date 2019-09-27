package com.sbt.rnd.meetup2017.transport.impl.server;

import java.util.List;

public interface TransportObjectFactory {

    Server createApiServer(String baseApiPackage, List<SpringServerApi.ApiServiceBean>  apiServices);

}
