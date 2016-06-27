package ru.alfabank.dmpr.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import ru.alfabank.dmpr.BaseDataContextTests;
import ru.alfabank.dmpr.infrastructure.spring.json.JodaCustomModule;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class JacksonTest extends BaseDataContextTests {

    @Test
    public void testCustomModuleWithLocalDateTime() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaCustomModule());

        Bean bean = new Bean(new LocalDateTime(2014, 1, 20, 0, 0));
        String string = mapper.writeValueAsString(bean);

        Bean clone = mapper.readValue(string, Bean.class);
        assertEquals(bean.start, clone.start);

        string = "{\"start\":\"2014-01-19T20:00:00.000Z\"}";
        clone = mapper.readValue(string, Bean.class);
        assertEquals(bean.start, clone.start);
    }

    @Test
    public void testCustomModuleWithLocalDate() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaCustomModule());

        Bean bean = new Bean(new LocalDate(2014, 1, 20));
        String string = mapper.writeValueAsString(bean);

        Bean clone = mapper.readValue(string, Bean.class);
        assertEquals(bean.end, clone.end);

        string = "{\"end\":\"2014-01-19T21:00:00.000Z\"}";
        clone = mapper.readValue(string, Bean.class);
        assertEquals(bean.end, clone.end);
    }

    public static class Bean {
        public LocalDateTime start;
        public LocalDate end;

        public Bean() {
        }

        public Bean(LocalDateTime start) {
            this.start = start;
        }

        public Bean(LocalDate end) {
            this.end = end;
        }

        @Override
        public String toString() {
            return "Bean{" +
                    "start=" + start +
                    ", end=" + end +
                    '}';
        }
    }
}
