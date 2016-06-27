package ru.alfabank.dmpr.infrastructure.spring.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ru.alfabank.dmpr.infrastructure.chart.Color;

import java.io.IOException;

/**
 * Сериализует экземпляры класса Color через вызов их метода toString
 */
public class ColorJsonSerializer extends StdSerializer<Color> {
    protected ColorJsonSerializer() {
        super(Color.class);
    }

    @Override
    public void serialize(Color value, JsonGenerator generator, SerializerProvider provider)
            throws IOException, JsonGenerationException {
        if (value != null)
            generator.writeString(value.toString());
        else
            generator.writeNull();
    }
}
