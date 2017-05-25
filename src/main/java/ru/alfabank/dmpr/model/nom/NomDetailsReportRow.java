package ru.alfabank.dmpr.model.nom;

import org.joda.time.LocalDate;

/**
 * Строка отчета. Содержит все необходимые данные для выгрузки в Excel
 */
public class NomDetailsReportRow implements Cloneable{
    public LocalDate valueDay;
    public int periodNum;
    public String valueDayAsString;
    public String direction;
    public String divGrp;
    public String kpCCode;
    public String kpName;
    public String kp2CCode;
    public String kp2Name;
    public String ooDoc;
    public String coRp;
    public String region;
    public String blk;
    public String calcType;
    public double cnt;
    public String portfolioFlag;
    public String calcTypeName;

    public boolean isAbnormalEmission() {
        return isAbnormalEmission;
    }

    public void setIsAbnormalEmission(boolean isAbnormalEmission) {
        this.isAbnormalEmission = isAbnormalEmission;
    }

    private boolean isAbnormalEmission;

    public String getOperationDateKey(){
        return valueDay + "_" + direction + "_" + divGrp + "_" + kpCCode + "_" + kp2CCode + "_" + ooDoc + "_" + region + "_" + coRp;
    }

    public String getProductDateKey(){
        return valueDay + "_" + direction + "_" + divGrp + "_" + kpCCode + "_" + ooDoc + "_" + region + "_" + coRp;
    }

    public String getKey(){
        return direction + "_" + divGrp + "_" + kpCCode + "_" + kp2CCode + "_" + ooDoc + "_" + region + "_" + coRp;
    }

    @Override
    protected NomDetailsReportRow clone() throws CloneNotSupportedException {
        return (NomDetailsReportRow)super.clone();
    }

    public NomDetailsReportRow cloneWithoutAdditionalFields() {
        NomDetailsReportRow result;

        try{
            result = this.clone();
        } catch(CloneNotSupportedException e){
            result = new NomDetailsReportRow();
        }

        result.blk = null;
        result.calcType = null;
        result.portfolioFlag = null;

        return result;
    }
}
