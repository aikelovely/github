package ru.alfabank.dmpr.infrastructure.spring.json;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

/**
 * Регистрирует набор сериалайзеров и десериалайзеров для JodaTime
 */
public class JodaCustomModule extends SimpleModule {
    public JodaCustomModule() {
        addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        addSerializer(LocalDate.class, new LocalDateSerializer());

        addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        addDeserializer(LocalDate.class, new LocalDateDeserializer());
    }
}
