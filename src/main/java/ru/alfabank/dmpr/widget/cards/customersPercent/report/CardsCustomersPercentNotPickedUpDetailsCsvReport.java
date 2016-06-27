package ru.alfabank.dmpr.widget.cards.customersPercent.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.export.csv.CsvBuilder;
import ru.alfabank.dmpr.infrastructure.export.csv.fluent.ColumnFactory;
import ru.alfabank.dmpr.infrastructure.export.csv.fluent.ColumnFactoryWrapper;
import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentDetailsItem;
import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentOptions;
import ru.alfabank.dmpr.repository.cards.CardsCustomersPercentRepository;
import ru.alfabank.dmpr.widget.BaseCsvReport;
import ru.alfabank.dmpr.widget.BaseReport;

/**
 * "Детализированный отчет по не забранным картам" в формате CSV
 */
@Service
public class CardsCustomersPercentNotPickedUpDetailsCsvReport extends BaseCsvReport<CardsCustomersPercentOptions> {
    @Autowired
    private CardsCustomersPercentRepository repository;

    public CardsCustomersPercentNotPickedUpDetailsCsvReport() {
        super(CardsCustomersPercentOptions.class);
    }

    @Override
    protected String getReportName(CardsCustomersPercentOptions options) {
        return "NotPickedUpDetailsReport";
        //return "Детализированный отчет по не забранным картам";
    }

    @Override
    protected void configure(CsvBuilder builder, CardsCustomersPercentOptions options) {
        final CardsCustomersPercentDetailsItem[] data = repository.getKpi2UnTakenDetailsDataItems(options);

        builder.configure(CardsCustomersPercentDetailsItem.class).bindTo(data).columns(new ColumnFactoryWrapper() {
            @Override
            public void createColumns(ColumnFactory c) {
                c.add("cardNo").title("Номер карты");
                c.add("validFrom").title("Действует с").format("dd.MM.yyyy");
                c.add("validThru").title("Действует по").format("dd.MM.yyyy");
                c.add("cardActivityDuration").title("Срок действия карты");
                c.add("acceptDoDate").title("Дата перехода в статус поступила в отделение/поступила к выдаче")
                        .format("dd.MM.yyyy");
                c.add("accountOwnerCode").title("Код владельца счета");
                c.add("cardHolderCode").title("Код держателя карты");
                c.add("reportINN").title("ИНН");
                c.add("actionWithCard").title("Действие с картой");
                c.add("cardContractCode").title("Код контракта");
                c.add("cardContractName").title("Наименование контракта");
                c.add("sRVPack").title("Пакет услуг");
                c.add("cardType").title("Тип карты");
                c.add("cardKindName").title("Вид карты");
                c.add("cardCategory").title("Категория карты");
                c.add("debetOrCredit").title("Дебет/Кредит");
                c.add("orderDate").title("Дата заказа");
                c.add("cityName").title("Город отделения заказа");
                c.add("branchName").title("Отделение заказа");
                c.add("cardReadyDate").title("Дата готовности карты").format("dd.MM.yyyy");
                c.add("deliverCityName").title("Город отделения доставки");
                c.add("deliverBranchName").title("Отделение доставки");
                c.add("appUserName").title("Пользователь оформивший заявку");
            }
        });
    }
}
