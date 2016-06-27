package ru.alfabank.dmpr.infrastructure.mybatis.tracer;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import ru.alfabank.dmpr.infrastructure.spring.security.UserContext;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Interceptor для отслеживания sql-запросов, поступающих инфраструкуре MyBatis на исполнение
 * Отслеживает все запросы: те которые выполняются против БД, а также те которые удовлетворяются из кэша
 *
 */
@Intercepts({@Signature(type = Executor.class, method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class SqlQueryTracer implements Interceptor {
    // привязанный к каджому потоку список отслеженных запросов
    private static final ThreadLocal<List<TracingQuery>> queryList = new ThreadLocal<>();
    // флаг отключения работы
    private boolean disable;

    private static void addToQueryList(TracingQuery query) {
        List<TracingQuery> list = queryList.get();
        if (list == null)
            queryList.set(list = new ArrayList<>());
        list.add(query);
    }

    /**
     * Предоставляет список запросов отслеженных на текущем потоке
     * @return список запросов
     */
    public static List<TracingQuery> consumeQueryList() {
        List<TracingQuery> list = queryList.get();
        if (list != null)
            queryList.remove();
        return list;
    }

    /**
     * Интерфейсный метод извлекает из MyBatis конфигурации данного Interceptor значение флага disable
     * @param properties MyBatis конфигурация данного Interceptor
     */
    @Override
    public void setProperties(Properties properties) {
        disable = Boolean.valueOf(properties.getProperty("disable"));
    }

    /**
     * Интерфейсный метод, вызываемый инфраструктурой MyBatis.
     * Определяет текст sql-запроса, а также способ его удовлетворения: из БД или из кэша
     * @param target прерываемый объект
     * @return декоратор поверх прерываемого объекта
     */
    @Override
    public Object plugin(Object target) {
        // отрабатывается только один тип прерываемых объектов - CachingExecutor
        // т.к. только в этом случае, запросы приходят всегда, независимо от наличия данных в кэше
        if (disable || !(target instanceof CachingExecutor))
            return target;

        // работает только для администратора, а также в режиме разработке, когда аутентификация отключена
        if (!UserContext.isSqlViewerRole())
            return target;

        return new ForwardingExecutor((CachingExecutor) target) {
            // единственный реально прерываемый метод
            public <E> List<E> query(MappedStatement ms, Object parameterObject, RowBounds rowBounds,
                                     ResultHandler resultHandler) throws SQLException {

                // получение необходимых объектов инфраструктуры MyBatis
                Configuration configuration = ms.getConfiguration();
                BoundSql boundSql = ms.getBoundSql(parameterObject);
                ParameterHandler parameterHandler = configuration.newParameterHandler(ms, parameterObject, boundSql);

                // формирование sql-запроса с подставленными значениями параметров
                PreparedStatementCapture capture = new PreparedStatementCapture(boundSql.getSql());
                parameterHandler.setParameters(capture);
                String resultSql = capture.toString();

                // проверка присутствия результата sql-запроса в кэше
                CacheKey key = createCacheKey(ms, parameterObject, rowBounds, boundSql);
                Cache cache = ms.getCache();
                boolean fromCache = cache != null && cache.getObject(key) != null;

                // добавление запроса в список
                addToQueryList(new TracingQuery(resultSql, fromCache));

                return super.query(ms, parameterObject, rowBounds, resultHandler);
            }
        };
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        return invocation.proceed();
    }
}
