package ru.alfabank.dmpr.infrastructure.spring.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.io.IOException;

public class LocalDateSerializer extends StdSerializer<LocalDate> {
    public LocalDateSerializer() {
        super(LocalDate.class);
    }

    /**
     * Сериализует значение типа LocalDate как число миллисекунд since 1970-01-01T00:00:00Z
     * @param value значение
     * @param gen gen
     * @param provider provider
     * @throws IOException
     * @throws JsonGenerationException
     */
    @Override
    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider provider)
            throws IOException, JsonGenerationException {
        if (value != null) {
            gen.writeNumber(value.toDateTime(new LocalTime(0, 0)).getMillis());
        } else {
            gen.writeNull();
        }
    }
}
