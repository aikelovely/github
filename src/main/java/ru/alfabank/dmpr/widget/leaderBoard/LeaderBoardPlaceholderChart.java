package ru.alfabank.dmpr.widget.leaderBoard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.model.leaderBoard.ChartMetaData;
import ru.alfabank.dmpr.model.leaderBoard.LeaderBoardOptions;
import ru.alfabank.dmpr.repository.leaderBoard.LeaderBoardRepository;
import ru.alfabank.dmpr.widget.BaseWidget;

/**
 * Получает мета-данные для отрисовки графиков внутри блока
 */
@Service
public class LeaderBoardPlaceholderChart extends BaseWidget<LeaderBoardOptions, ChartMetaData[]> {
    @Autowired
    LeaderBoardRepository repository;

    public LeaderBoardPlaceholderChart() {
        super(LeaderBoardOptions.class);
    }

    @Override
    public ChartMetaData[] getData(LeaderBoardOptions options) {
        return repository.getLayout(options);
    }
}
