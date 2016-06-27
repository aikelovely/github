package ru.alfabank.dmpr.model.mass.decomposition;

public enum TresholdCode {
    AvgDuration {
        public String dbCode() { return "AVG_LENINHOUR"; }
    },
    ByDay {
        public String dbCode() { return "DGRP_DAY_BP_RATIO_PCT"; }
    },
    By3Days {
        public String dbCode() { return "DGRP_3DAY_BP_RATIO_PCT"; }
    },
    CRMRatio {
        public String dbCode() { return "IS_IN_CRM_RATIO_PCT"; }
    };

    public abstract String dbCode();
}
