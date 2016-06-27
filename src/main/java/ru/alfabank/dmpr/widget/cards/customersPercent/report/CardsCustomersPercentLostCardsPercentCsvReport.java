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

/**
 * "Детализированный отчет по потерянным картам" в формате CSV
 */
@Service
public class CardsCustomersPercentLostCardsPercentCsvReport extends BaseCsvReport<CardsCustomersPercentOptions> {
    @Autowired
    private CardsCustomersPercentRepository repository;

    public CardsCustomersPercentLostCardsPercentCsvReport() {
        super(CardsCustomersPercentOptions.class);
    }

    @Override
    protected String getReportName(CardsCustomersPercentOptions options) {
        return "LostCardsReport";
        //return "Отчет по потерянным картам";
    }

    @Override
    protected void configure(CsvBuilder builder, CardsCustomersPercentOptions options) {
        final CardsCustomersPercentDetailsItem[] data = repository.getKpi2LostCardsDetailsDataItems(options);

        builder.configure(CardsCustomersPercentDetailsItem.class).bindTo(data).columns(new ColumnFactoryWrapper() {
            @Override
            public void createColumns(ColumnFactory c) {
                c.add("cardNo").title("Номер карты");
                c.add("reportINN").title("ИНН");
                c.add("validFrom").title("Действует с").format("dd.MM.yyyy");
                c.add("validThru").title("Действует по").format("dd.MM.yyyy");
                c.add("accountOwnerCode").title("Код владельца счета");
                c.add("cardHolderCode").title("Код владельца карты");
                c.add("actionWithCard").title("Действие с картой");
                c.add("cardContractCode").title("Код контракта");
                c.add("cardContractName").title("Наименование контракта");
                c.add("cardType").title("Тип карты");
                c.add("cardKindName").title("Вид карты");
                c.add("cardCategory").title("Категория карты");
                c.add("debetOrCredit").title("Дебет/Кредит");
                c.add("sRVPack").title("Пакет услуг");
                c.add("orderDate").title("Дата заказа").format("dd.MM.yyyy");
                c.add("cityName").title("Город отделения заказа");
                c.add("branchName").title("Отделение заказа");
                c.add("cardReadyDate").title("Дата готовности карты").format("dd.MM.yyyy");
                c.add("deliverCityName").title("Город отделения доставки");
                c.add("deliverBranchName").title("Отделение доставки");
            }
        });
    }
}
