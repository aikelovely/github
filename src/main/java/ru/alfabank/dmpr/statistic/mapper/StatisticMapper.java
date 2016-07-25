package ru.alfabank.dmpr.statistic.mapper;


import org.apache.ibatis.annotations.Param;
import ru.alfabank.dmpr.statistic.Statistic;

public interface StatisticMapper {

    void insertStatistic(@Param("statistics") Statistic[] statistics);
}
