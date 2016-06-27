package ru.alfabank.dmpr.infrastructure.spring.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import java.io.IOException;

public class LocalDateTimeSerializer extends StdSerializer<LocalDateTime> {
    public LocalDateTimeSerializer() {
        super(LocalDateTime.class);
    }

    /**
     * Сериализует значение типа LocalDateTime как число миллисекунд since 1970-01-01T00:00:00Z
     * @param value значение
     * @param gen gen
     * @param provider provider
     * @throws IOException
     * @throws JsonGenerationException
     */
    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider)
            throws IOException, JsonGenerationException {
        if (value != null) {
            gen.writeNumber(value.toDateTime().getMillis());
        } else {
            gen.writeNull();
        }
    }
}
