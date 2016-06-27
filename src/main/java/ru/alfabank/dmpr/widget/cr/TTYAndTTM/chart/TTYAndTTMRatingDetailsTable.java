package ru.alfabank.dmpr.widget.cr.TTYAndTTM.chart;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.model.cr.TTYAndTTM.Rating;
import ru.alfabank.dmpr.model.cr.TTYAndTTM.TTYAndTTMOptions;
import ru.alfabank.dmpr.repository.cr.TTYAndTTMRepository;
import ru.alfabank.dmpr.widget.BaseWidget;

/**
 * Таблица с детализацией рейтинга среднего времени/процента в KPI
 */
@Service
public class TTYAndTTMRatingDetailsTable extends BaseWidget<TTYAndTTMOptions, Rating[]> {
    @Autowired
    TTYAndTTMRepository repository;

    public TTYAndTTMRatingDetailsTable() {
        super(TTYAndTTMOptions.class);
    }

    @Override
    public Rating[] getData(final TTYAndTTMOptions options) {
        return repository.getRatingDetails(options);
    }
}