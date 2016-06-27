SELECT
            TimeUnitDD calcDate,
            UnitCode,
            UnitName,
            DurGRPDays DayGroupId,
            DurGRPDaysName DayGroupName,
            RegionId as macroRegionId,
            RegionId,
            CityId,
            DOCODE salePlaceId,
            CardCount,
            InKPICardCount,
            TotalDuration,
            ReissueSpeedID,
            ReissueSpeedDays,
            KPIDurationDays DurationNormative,
            KPIRateTreshold PercentNormative
        FROM TABLE (pkg_zp_card_api.get_kpi1_data (
        p_DateFrom             => ?, -- Дата начала периода? Дата? окончания периода
        p_DateTill             => ?, -- Дата окончания периода
        p_RegionIDs            => ?, -- ИД регионов
        p_CityIDs              => ?, -- ИД городов
        p_BranchTypeIDs        => ?, -- Типы ДО
        p_DOCodes              => ?, -- ИД ДО
        p_ZPTypeIDs            => ?, -- Типы ЗП
        p_SrvPackCodes         => ?, -- Типы ПУ
        p_CardCategoryIDs      => ?, -- Категории карт
        p_DCCodes              => 'Who?', -- Дебит/Кредит
        p_ClientSegmentIDs     => '?', -- Клиентские сегменты
        p_DimesionID           => '?er', -- Уровень деталзации (1-регион,2-город,3 - ДО)
        p_TimeUnitID           => '??ff??',   -- Уровень деталзации по времени
        p_IsDurGrp             => ?,    -- Детализировать по длительности (1)
        p_IsAutoProlongation   => ?, -- Фильтр автопролонгации (1 - только АП, иное - все, кроме ПА).
        p_mvk                  => ?          --  Признак МВК
        ))