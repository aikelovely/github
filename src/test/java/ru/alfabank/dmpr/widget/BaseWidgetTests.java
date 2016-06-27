package ru.alfabank.dmpr.widget;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.alfabank.dmpr.BaseTests;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:app-context.xml")
public abstract class BaseWidgetTests extends BaseTests {
    private final ObjectMapper jsonMapper = new ObjectMapper();

    protected byte[] toJsonBytes(Object options) throws JsonProcessingException {
        return jsonMapper.writeValueAsBytes(options);
    }

    protected String toJsonString(Object options) throws JsonProcessingException {
        return jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(options);
    }
}
