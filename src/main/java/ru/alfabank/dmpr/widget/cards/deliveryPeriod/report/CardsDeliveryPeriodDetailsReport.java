package ru.alfabank.dmpr.widget.cards.deliveryPeriod.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.export.excel.ReportBuilder;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactory;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactoryWrapper;
import ru.alfabank.dmpr.model.cards.deliveryPeriod.CardsDeliveryPeriodDetailsItem;
import ru.alfabank.dmpr.model.cards.deliveryPeriod.CardsDeliveryPeriodOptions;
import ru.alfabank.dmpr.repository.cards.CardsDeliveryPeriodRepository;
import ru.alfabank.dmpr.widget.BaseReport;

@Service
public class CardsDeliveryPeriodDetailsReport extends BaseReport<CardsDeliveryPeriodOptions> {
    @Autowired
    private CardsDeliveryPeriodRepository repository;

    public CardsDeliveryPeriodDetailsReport() {
        super(CardsDeliveryPeriodOptions.class);
    }

    @Override
    protected String getReportName(final CardsDeliveryPeriodOptions options) {
        return "CardsDeliveryPeriodDetailsReport";
//        return options.automaticExtensionMode ?
//                "Детализированный отчет по картам со сроком доставки более трех недель"
//                : String.format("Детализированный отчет по картам со сроком доставки более %s дней",
//                options.daysLowerBound);
    }

    @Override
    protected void configure(ReportBuilder builder, final CardsDeliveryPeriodOptions options) {
        CardsDeliveryPeriodDetailsItem[] data = repository.getDetailsReportItems(options);

        builder.addWorksheet(CardsDeliveryPeriodDetailsItem.class)
                .bindTo(data)
                .title("Отчет")
                .columns(new ColumnFactoryWrapper() {
                    @Override
                    public void createColumns(ColumnFactory c) {
                        c.add("accountOwnerCode").title("Код владельца счета").width(20);
                        c.add("cardHolderCode").title("Код держателя карты").width(20);
                        c.add("cardNo").title("Номер карты").width(20);
                        c.add("reportINN").title("ИНН").width(20);
                        c.add("validThru").title("Срок действия карты").format("dd.MM.yyyy");
                        c.add("creationDate").title("Дату создания заявки на выпуск карты").format("dd.MM.yyyy");
                        c.add("acceptDate").title("Дата перехода в статус Поступила в отделение \\ Поступила к выдаче")
                                .width(30).format("dd.MM.yyyy");

                        c.add("cardContractCode").title("Код контракта");
                        c.add("cardContractName").title("Наименование контракта");
                        c.add("sRVPack").title("Пакет услуг");
                        c.add("cardType").title("Тип карты");
                        c.add("cardKindName").title("Вид карты");
                        c.add("cardCategory").title("Категория карты");
                        c.add("debetOrCredit").title("Дебет/Кредит");

                        if (options.automaticExtensionMode) {
                            c.add("reissueSpeedDays").title("Кол-во дней (срок доставки)").width(17);
                        } else {
                            c.add("duration").title("Кол-во дней (срок доставки)").width(17);
                        }

                        c.add("branchId").title("Отделение эмитент").width(17);
                        c.add("branchName").title("Отделение доставки карты");
                        c.add("cityName").title("Город");
                        c.add("regionName").title("Регион");
                    }
                });
    }
}
