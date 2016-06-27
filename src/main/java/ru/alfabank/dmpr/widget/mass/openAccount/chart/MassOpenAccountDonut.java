package ru.alfabank.dmpr.widget.mass.openAccount.chart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.chart.Color;
import ru.alfabank.dmpr.infrastructure.chart.Point;
import ru.alfabank.dmpr.infrastructure.chart.Series;
import ru.alfabank.dmpr.model.mass.openAccount.KpiDataItem;
import ru.alfabank.dmpr.model.mass.openAccount.MassOpenAccountOptions;
import ru.alfabank.dmpr.repository.mass.MassOpenAccountRepository;
import ru.alfabank.dmpr.widget.BaseChart;

@Service
public class MassOpenAccountDonut extends BaseChart<MassOpenAccountOptions> {
    @Autowired
    MassOpenAccountRepository repository;

    public MassOpenAccountDonut() {
        super(MassOpenAccountOptions.class);
    }

    @Override
    public ChartResult[] getData(MassOpenAccountOptions options) {
        KpiDataItem[] data = repository.getDonut(options);

        if(data == null || data.length == 0) {
            return new ChartResult[]{new ChartResult(new Series[0])};
        }

        KpiDataItem first = data[0];

        Point[] points = new Point[]{
                Point.withY((double)first.bpCountGrp1, "за час и менее", Color.BlueColor),
                Point.withY((double)first.bpCountGrp2, "от 1 часа до 24-х часов", Color.GreenColor),
                Point.withY((double)first.bpCountGrp3, "от 24-х часов до 3-х дней", Color.OrangeColor),
                Point.withY((double)first.bpCountGrp4, "более 3-х дней", Color.RedColor)
        };

        return new ChartResult[]{new ChartResult(new Series[]{new Series(points)})};
    }
}
