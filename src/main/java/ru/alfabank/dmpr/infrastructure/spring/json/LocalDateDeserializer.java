package ru.alfabank.dmpr.infrastructure.spring.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.io.IOException;

public class LocalDateDeserializer extends StdDeserializer<LocalDate> {
    public LocalDateDeserializer() {
        super(LocalDate.class);
    }

    /**
     * Десериализует в LocalDate из двух видов представления:
     * - строки в формате yyyy-mm-ddThh:mm:ss.000Z
     * - числа миллисекунд since 1970-01-01T00:00:00Z
     * @param parser parser
     * @param context context
     * @return LocalDate
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Override
    public LocalDate deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        DateTime dateTime;

        switch (parser.getCurrentToken()) {
            case VALUE_STRING:
                String string = parser.getText().trim();
                if (string.length() == 0)
                    return null;

                if (!string.endsWith("Z"))
                    throw new IllegalArgumentException(string);

                dateTime = new DateTime(string);
                return new LocalDate(dateTime);

            case VALUE_NUMBER_INT:
                dateTime = new DateTime(parser.getLongValue());
                return new LocalDate(dateTime);

            default:
                throw context.wrongTokenException(parser, JsonToken.VALUE_STRING, "expected JSON String or Number");
        }
    }
}
