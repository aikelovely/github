package ru.alfabank.dmpr.infrastructure.helper.dev;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public abstract class BaseProxyBean<T> implements FactoryBean<T>, InitializingBean {
    private final Class<T> objectClass;
    private T proxy;

    protected BaseProxyBean(Class<T> objectClass) {
        this.objectClass = objectClass;
    }

    protected abstract T newProxy();

    @Override
    public void afterPropertiesSet() throws Exception {
        proxy = newProxy();
    }

    @Override
    public T getObject() throws Exception {
        return proxy;
    }

    @Override
    public Class<?> getObjectType() {
        return proxy != null ? proxy.getClass() : objectClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
