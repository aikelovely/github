package ru.alfabank.dmpr.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import ru.alfabank.dmpr.BaseTests;
import ru.alfabank.dmpr.infrastructure.chart.Color;
import ru.alfabank.dmpr.model.cards.deliveryPeriod.CardsDeliveryPeriodOptions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:mvc-config.xml")
@Configuration
public class FrontApiControllerTests extends BaseTests {
    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private ObjectMapper jsonMapper;

    @Autowired
    protected WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.context).build();
    }

    @Test
    public void filter() throws Exception {
        mockMvc.perform(get("/api/cardsDeliveryPeriodFilter/regions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void widget() throws Exception {
        mockMvc.perform(post("/api/cardsDeliveryPeriodPie").content(toJsonBytes(getOptions())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    private CardsDeliveryPeriodOptions getOptions() {
        CardsDeliveryPeriodOptions options = new CardsDeliveryPeriodOptions();
        options.startDate = new LocalDate(2014, 1, 28);
        options.endDate = new LocalDate(2014, 2, 6);
        return options;
    }


    @Test
    public void testJodaWidget() throws Exception {
        LocalDateTime start = new LocalDateTime();
        Color color = Color.BlueColor;

        TestOptions options = new TestOptions(start, color);
        long millis = start.toDateTime().getMillis();

        mockMvc.perform(post("/api/TestWidget").content(toJsonBytes(options)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.start").value(millis))
                .andExpect(jsonPath("$.data.color").value(color.toString()))
                .andExpect(jsonPath("$.data.colorNull").doesNotExist())
        ;
    }

    private byte[] toJsonBytes(Object options) throws JsonProcessingException {
        return jsonMapper.writeValueAsBytes(options);
    }
}
