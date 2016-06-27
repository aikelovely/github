package ru.alfabank.dmpr.widget.cards.deliveryPeriod.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.export.csv.CsvBuilder;
import ru.alfabank.dmpr.infrastructure.export.csv.fluent.ColumnFactory;
import ru.alfabank.dmpr.infrastructure.export.csv.fluent.ColumnFactoryWrapper;
import ru.alfabank.dmpr.model.cards.deliveryPeriod.CardsDeliveryPeriodDetailsItem;
import ru.alfabank.dmpr.model.cards.deliveryPeriod.CardsDeliveryPeriodOptions;
import ru.alfabank.dmpr.repository.cards.CardsDeliveryPeriodRepository;
import ru.alfabank.dmpr.widget.BaseCsvReport;

@Service
public class CardsDeliveryPeriodDetailsCsvReport extends BaseCsvReport<CardsDeliveryPeriodOptions> {
    @Autowired
    private CardsDeliveryPeriodRepository repository;

    public CardsDeliveryPeriodDetailsCsvReport() {
        super(CardsDeliveryPeriodOptions.class);
    }

    @Override
    protected String getReportName(final CardsDeliveryPeriodOptions options) {
        return "CardsDeliveryPeriodDetailsReport";
    }

    @Override
    protected void configure(CsvBuilder builder, final CardsDeliveryPeriodOptions options) {
        CardsDeliveryPeriodDetailsItem[] data = repository.getDetailsReportItems(options);

        builder.configure(CardsDeliveryPeriodDetailsItem.class)
                .bindTo(data)
                .columns(new ColumnFactoryWrapper() {
                    @Override
                    public void createColumns(ColumnFactory c) {
                        c.add("accountOwnerCode").title("Код владельца счета");
                        c.add("cardHolderCode").title("Код держателя карты");
                        c.add("cardNo").title("Номер карты");
                        c.add("reportINN").title("ИНН");
                        c.add("validThru").title("Срок действия карты").format("dd.MM.yyyy");
                        c.add("creationDate").title("Дату создания заявки на выпуск карты").format("dd.MM.yyyy");
                        c.add("acceptDate").title("Дата перехода в статус Поступила в отделение \\ Поступила к выдаче")
                                .format("dd.MM.yyyy");

                        c.add("cardContractCode").title("Код контракта");
                        c.add("cardContractName").title("Наименование контракта");
                        c.add("sRVPack").title("Пакет услуг");
                        c.add("cardType").title("Тип карты");
                        c.add("cardKindName").title("Вид карты");
                        c.add("cardCategory").title("Категория карты");
                        c.add("debetOrCredit").title("Дебет/Кредит");

                        if (options.automaticExtensionMode) {
                            c.add("reissueSpeedDays").title("Кол-во дней (срок доставки)");
                        } else {
                            c.add("duration").title("Кол-во дней (срок доставки)");
                        }

                        c.add("branchId").title("Отделение эмитент");
                        c.add("branchName").title("Отделение доставки карты");
                        c.add("cityName").title("Город");
                        c.add("regionName").title("Регион");
                    }
                });
    }
}
