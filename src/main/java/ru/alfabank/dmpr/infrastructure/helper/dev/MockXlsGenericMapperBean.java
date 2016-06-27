package ru.alfabank.dmpr.infrastructure.helper.dev;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static java.lang.reflect.Proxy.newProxyInstance;

/**
 * Generic фабричный класс, создающий прокси объект для запросшенного generic типа,
 * вызовы методов которого адресуются репозиторию моковых данных, сохраненных в Excel-файлах.
 * Используется для мокирования mapper-ов MyBatis
 * @param <T> запросшенный generic тип
 */
public class MockXlsGenericMapperBean<T> extends BaseProxyBean<T> {
    private final Class<T> objectClass;
    private final MockXlsRepository repository = new MockXlsRepository();

    public MockXlsGenericMapperBean(Class<T> objectClass) {
        super(objectClass);
        this.objectClass = objectClass;
    }

    @Override
    protected T newProxy() {
        Object proxy = newProxyInstance(
                objectClass.getClassLoader(),
                new Class[]{objectClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        return repository.read(objectClass, method, args);
                    }
                });

        @SuppressWarnings("unchecked")
        T result = (T) proxy;

        return result;
    }

    public static <T> T createProxy(Class<T> objectClass) {
        @SuppressWarnings("unchecked")
        MockXlsGenericMapperBean factory = new MockXlsGenericMapperBean(objectClass);
        try {
            factory.afterPropertiesSet();
            @SuppressWarnings("unchecked")
            T proxy = (T) factory.newProxy();
            return proxy;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
