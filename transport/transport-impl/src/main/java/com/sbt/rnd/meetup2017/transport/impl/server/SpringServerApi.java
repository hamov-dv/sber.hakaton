package com.sbt.rnd.meetup2017.transport.impl.server;

import com.sbt.rnd.meetup2017.transport.api.Api;
import com.sbt.rnd.meetup2017.transport.api.RequestApiImpl;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.SpringProxy;
import org.springframework.aop.TargetClassAware;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContextException;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Proxy;
import java.util.*;

public class SpringServerApi implements Server, ApplicationContextAware, InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(SpringServerApi.class);
    private Server server;
    protected ApplicationContext applicationContext;
    private final TransportObjectFactory objectFactory;
    private final String baseApiPackage;
    private final String baseServicesPackage;
    private boolean autoStart;

    public SpringServerApi(TransportObjectFactory objectFactory, String baseApiPackage, String baseServicesPackage) {
        this.autoStart = true;
        this.objectFactory = objectFactory;
        this.baseApiPackage = baseApiPackage;
        this.baseServicesPackage = baseServicesPackage;

    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void afterPropertiesSet() throws Exception {

        this.server = this.objectFactory.createApiServer(this.baseApiPackage, this.getApiServices(this.baseApiPackage, this.baseServicesPackage));


        if (this.isAutoStart()) {
            logger.debug("Auto start API server...");
            this.startServer();
            logger.debug("Auto start API server done.");
        }

    }

    public void destroy() throws Exception {
        this.stopServer();
    }

    private Class getBeanClass(Object bean) {

        if (isAopProxy(bean) && bean instanceof Advised) {

            Class clazz = ((Advised) bean).getTargetSource().getTargetClass();
            if (clazz != null) {
                return clazz;
            }
        }
        if (bean instanceof TargetClassAware) {
            Class clazz = ((TargetClassAware) bean).getTargetClass();
            if (clazz != null) {
                return clazz;
            }
        }
        return bean.getClass();
    }

    private boolean isAopProxy(Object object) {
        return (object instanceof SpringProxy && (Proxy.isProxyClass(object.getClass()) || ClassUtils.isCglibProxy(object.getClass())));
    }

    private Set<Class<?>> getApiClasses(String baseApiPackage, String baseServicesPackage) {
        ArrayList apiServices = new ArrayList();
        Reflections reflections = new Reflections(baseApiPackage + ".");
        return reflections.getTypesAnnotatedWith(Api.class, true);

    }

    private List<ApiServiceBean> getApiServices(String baseApiPackage, String baseServicesPackage) {
        ArrayList apiServices = new ArrayList();
        Reflections reflections = new Reflections(baseApiPackage + ".");
        Set apiClasses = reflections.getTypesAnnotatedWith(Api.class, true);
        Iterator var6 = apiClasses.iterator();

        Class apiClass;
        boolean implemented;
        label62:
        do {
            if (!var6.hasNext()) {
                return apiServices;
            }

            apiClass = (Class) var6.next();
            implemented = false;
            Map apiBeans = this.applicationContext.getBeansOfType(apiClass);

            Iterator var12 = apiBeans.entrySet().iterator();

            while (true) {
                Map.Entry apiBean;
                Class apiBeanClass;
                do {
                    do {
                        if (!var12.hasNext()) {
                            continue label62;
                        }

                        apiBean = (Map.Entry) var12.next();
                        apiBeanClass = getBeanClass(apiBean.getValue());
                    } while (!apiBeanClass.isAnnotationPresent(RequestApiImpl.class));
                } while (!apiBeanClass.getPackage().getName().equals(baseServicesPackage));

                if (implemented) {
                    throw new IllegalArgumentException(String.format("Found two implementations marked @ApiService (%s, %s) for one @Api (%s)", new Object[]{apiServices.get(apiServices.size() - 1).getClass().getSimpleName(), apiBeanClass.getSimpleName(), apiClass.getName()}));
                }

                if (apiClass.isAssignableFrom(apiBeanClass)) {
                    apiServices.add(new ApiServiceBean(apiClass,apiBeanClass,apiBean.getValue()));
                }

                implemented = true;
            }
        } while (implemented);

        throw new ApplicationContextException("No implementation marked @RequestApiImpl founded in context for @Api interface: " + apiClass.getName());
    }

    public boolean isAutoStart() {
        return this.autoStart;
    }

    public void setAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
    }

    public void startServer() {
        this.server.startServer();
    }

    public void stopServer() {
        this.server.stopServer();
    }

    public static class ApiServiceBean {

        private final Class apiClass;
        private final Class apiServiceClass;
        private final Object bean;

        public ApiServiceBean(Class apiClass, Class apiServiceClass, Object bean) {
            this.apiClass = apiClass;
            this.apiServiceClass = apiServiceClass;
            this.bean = bean;
        }

        public Class getApiClass() {
            return apiClass;
        }

        public Class getApiServiceClass() {
            return apiServiceClass;
        }

        public Object getBean() {
            return bean;
        }
    }
}
