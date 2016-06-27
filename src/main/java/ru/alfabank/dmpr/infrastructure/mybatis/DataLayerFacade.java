package ru.alfabank.dmpr.infrastructure.mybatis;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.ApplicationContext;
import ru.alfabank.dmpr.infrastructure.spring.ApplicationContextProvider;

public class DataLayerFacade {
    private DataLayerFacade() {
    }

    /**
     * Очищает все кэши данных управляемые инфраструктурой MyBatis
     */
    public static void clearCaches() {
        ApplicationContext context = ApplicationContextProvider.getApplicationContext();
        SqlSessionFactory sqlSessionFactory = context.getBean(SqlSessionFactory.class);
        Configuration configuration = sqlSessionFactory.getConfiguration();

        for (Cache cache : configuration.getCaches()) {
            cache.clear();
        }
    }
}
