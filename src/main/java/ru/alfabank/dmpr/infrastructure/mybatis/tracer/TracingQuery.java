package ru.alfabank.dmpr.infrastructure.mybatis.tracer;

/**
 * Структура для хранения текста sql-запроса и флага отражающего факт, что вместо выполения
 * запрса к БД данные дня него были получены из кэша
 */
public class TracingQuery {
    public final String sql;
    public final boolean fromCache;

    public TracingQuery(String sql, boolean fromCache) {
        this.sql = sql;
        this.fromCache = fromCache;
    }

    @Override
    public String toString() {
        return "TracingQuery{" +
                "fromCache=" + fromCache +
                ", sql='" + sql + '\'' +
                '}';
    }
}
