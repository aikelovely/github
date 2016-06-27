package ru.alfabank.dmpr.widget.cards;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockServletContext;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.export.ReportBuilderResult;
import ru.alfabank.dmpr.model.cards.deliveryPeriod.CardsDeliveryPeriodOptions;
import ru.alfabank.dmpr.widget.BaseWidgetTests;
import ru.alfabank.dmpr.widget.cards.deliveryPeriod.chart.CardsDeliveryPeriodDistributionDynamic;
import ru.alfabank.dmpr.widget.cards.deliveryPeriod.chart.CardsDeliveryPeriodDynamic;
import ru.alfabank.dmpr.widget.cards.deliveryPeriod.chart.CardsDeliveryPeriodPie;
import ru.alfabank.dmpr.widget.cards.deliveryPeriod.chart.CardsDeliveryPeriodTable;
import ru.alfabank.dmpr.widget.cards.deliveryPeriod.report.CardsDeliveryPeriodDetailsReport;
import ru.alfabank.dmpr.widget.cards.deliveryPeriod.report.CardsDeliveryPeriodDistributionDynamicReport;

import javax.servlet.ServletContext;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotEquals;

@Configuration
public class DeliveryPeriodWidgetTests extends BaseWidgetTests {
    @Autowired
    private CardsDeliveryPeriodPie pieWidget;
    @Autowired
    private CardsDeliveryPeriodDynamic dynamicWidget;
    @Autowired
    private CardsDeliveryPeriodDistributionDynamic distributionWidget;
    @Autowired
    private CardsDeliveryPeriodTable tableWidget;
    @Autowired
    private CardsDeliveryPeriodDistributionDynamicReport distributionReportWidget;
    @Autowired
    private CardsDeliveryPeriodDetailsReport detailsReportWidget;

    @Bean
    public static ServletContext getServletContext() {
        return new MockServletContext() {
            @Override
            public String getRealPath(String path) {
                return path;
            }
        };
    }

    @Test
    public void testPieWidget() throws Exception {
        ChartResult[] data = pieWidget.getData(createOptions());
        assertNotEquals(0, data.length);
    }

    @Test
    public void testDynamicWidget() throws Exception {
        CardsDeliveryPeriodOptions options = createOptions();
        ChartResult[] data = dynamicWidget.getData(options);
        assertNotEquals(0, data.length);
    }

    @Test
    public void testDistributionDynamicWidgetModeFalse() throws Exception {
        CardsDeliveryPeriodOptions options = createOptions();
        options.automaticExtensionMode = false;
        ChartResult[] data = distributionWidget.getData(options);
        assertNotEquals(0, data.length);
    }

    @Test
    public void testDistributionDynamicWidgetModeTrue() throws Exception {
        CardsDeliveryPeriodOptions options = createOptions();
        options.automaticExtensionMode = true;
        ChartResult[] data = distributionWidget.getData(options);
        assertNotEquals(0, data.length);
    }

    @Test
    public void testTableWidget() throws Exception {
        List<Map<String, Object>> data = tableWidget.getData(createOptions());
        assertNotEquals(0, data.size());
    }

    @Test
    public void testDistributionDynamicReportWidgetModeFalse() throws Exception {
        CardsDeliveryPeriodOptions options = createOptions();
        options.automaticExtensionMode = false;
        ReportBuilderResult data = distributionReportWidget.getData(options);
        assertNotEquals(0, data.getId());
    }

    @Test
    public void testDistributionDynamicReportWidgetModeTrue() throws Exception {
        CardsDeliveryPeriodOptions options = createOptions();
        options.automaticExtensionMode = true;
        ReportBuilderResult data = distributionReportWidget.getData(options);
        assertNotEquals(0, data.getId());
    }

    @Test
    public void testDetailsReportWidget() throws Exception {
        CardsDeliveryPeriodOptions options = new CardsDeliveryPeriodOptions();
        options.startDate = new LocalDate(2014, 1, 28);
        options.endDate = new LocalDate(2014, 1, 28);
        options.timeUnitId = 5;
        options.regionIds = new long[]{1};
        options.cityIds = new long[]{82};

        ReportBuilderResult data = detailsReportWidget.getData(options);
        assertNotEquals(0, data.getId());
        System.out.println(toJsonString(data));
    }

    private CardsDeliveryPeriodOptions createOptions() {
        CardsDeliveryPeriodOptions options = new CardsDeliveryPeriodOptions();
        options.startDate = new LocalDate(2014, 1, 28);
        options.endDate = new LocalDate(2014, 2, 6);
        options.timeUnitId = 2;
        return options;
    }
}