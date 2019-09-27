package com.sbt.rnd.meetup2017.transport.impl.client;

import com.sbt.rnd.meetup2017.transport.api.TransportRequest;
import com.sbt.rnd.meetup2017.transport.impl.Rpc;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class RequestInterceptor<T> implements MethodInterceptor {

    private Rpc rpc;

    public RequestInterceptor() {
    }

    public Rpc getRpc() {
        return rpc;
    }

    public void setRpc(Rpc rpc) {
        this.rpc = rpc;
    }

    private class RequestImpl<T> implements TransportRequest<T> {

        private final MethodInvocation methodInvocation;

        public RequestImpl(MethodInvocation methodInvocation) {
            this.methodInvocation = methodInvocation;
        }

        @Override
        public T call() {

            return rpc.callMethod(methodInvocation.getMethod().getName(), methodInvocation.getArguments(), methodInvocation.getMethod().getParameterTypes());
        }

    }

    protected Object createRequest(MethodInvocation invocation) {
        return new RequestImpl(invocation);
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        // Перехватываем вызовы к методам Object
        if (methodInvocation.getMethod().getDeclaringClass().equals(Object.class)) {
            String methodName = methodInvocation.getMethod().getName();

            return null;
        }

        return createRequest(methodInvocation);
    }
}
