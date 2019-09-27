package com.sbt.rnd.meetup2017.transport.message;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class MessageProperties implements Serializable {

    private volatile String id;
    private volatile String method;
    private volatile String apiName;
    private volatile String reqId;
    private volatile Date date;
    private volatile String nodeId;
    private volatile String moduleId;
    private volatile String destination;
    private volatile Long timeout;

    public MessageProperties() {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        return "MessageProperties{" +
                "id='" + id + '\'' +
                ", method='" + method + '\'' +
                ", apiName='" + apiName + '\'' +
                ", reqId='" + reqId + '\'' +
                ", date=" + date +
                ", nodeId='" + nodeId + '\'' +
                ", moduleId='" + moduleId + '\'' +
                ", destination='" + destination + '\'' +
                '}';
    }
}
