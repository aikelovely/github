package ru.alfabank.dmpr.widget.cards.customersPercent.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.export.excel.ReportBuilder;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactory;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactoryWrapper;
import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentDetailsItem;
import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentOptions;
import ru.alfabank.dmpr.repository.cards.CardsCustomersPercentRepository;
import ru.alfabank.dmpr.widget.BaseReport;

/**
 * "Детализированный отчет по не забранным картам" в формате Excel
 */
@Service
public class CardsCustomersPercentNotPickedUpDetailsReport extends BaseReport<CardsCustomersPercentOptions> {
    @Autowired
    private CardsCustomersPercentRepository repository;

    public CardsCustomersPercentNotPickedUpDetailsReport() {
        super(CardsCustomersPercentOptions.class);
    }

    @Override
    protected String getReportName(CardsCustomersPercentOptions options) {
        return "NotPickedUpDetailsReport";
        //return "Детализированный отчет по не забранным картам";
    }

    @Override
    protected void configure(ReportBuilder builder, CardsCustomersPercentOptions options) {
        final CardsCustomersPercentDetailsItem[] data = repository.getKpi2UnTakenDetailsDataItems(options);

        builder.addWorksheet(CardsCustomersPercentDetailsItem.class).bindTo(data).columns(new ColumnFactoryWrapper() {
            @Override
            public void createColumns(ColumnFactory c) {
                c.add("cardNo").title("Номер карты");
                c.add("validFrom").title("Действует с").format("dd.MM.yyyy").width(15);
                c.add("validThru").title("Действует по").format("dd.MM.yyyy").width(15);
                c.add("cardActivityDuration").title("Срок действия карты").width(25);
                c.add("acceptDoDate").title("Дата перехода в статус поступила в отделение/поступила к выдаче")
                        .format("dd.MM.yyyy").width(25);
                c.add("accountOwnerCode").title("Код владельца счета").width(23);
                c.add("cardHolderCode").title("Код держателя карты").width(23);
                c.add("reportINN").title("ИНН").width(20);
                c.add("actionWithCard").title("Действие с картой").width(25);
                c.add("cardContractCode").title("Код контракта");
                c.add("cardContractName").title("Наименование контракта");
                c.add("sRVPack").title("Пакет услуг");
                c.add("cardType").title("Тип карты").width(15);
                c.add("cardKindName").title("Вид карты");
                c.add("cardCategory").title("Категория карты").width(20);
                c.add("debetOrCredit").title("Дебет/Кредит").width(20);
                c.add("orderDate").title("Дата заказа").format("dd.MM.yyyy").width(20);
                c.add("cityName").title("Город отделения заказа").width(30);
                c.add("branchName").title("Отделение заказа");
                c.add("cardReadyDate").title("Дата готовности карты").format("dd.MM.yyyy").width(20);
                c.add("deliverCityName").title("Город отделения доставки").width(30);
                c.add("deliverBranchName").title("Отделение доставки");
                c.add("appUserName").title("Пользователь оформивший заявку").width(30);
            }
        });
    }
}
