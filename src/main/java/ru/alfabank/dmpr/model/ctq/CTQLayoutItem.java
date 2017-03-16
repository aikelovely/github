package ru.alfabank.dmpr.model.ctq;

public class CTQLayoutItem {
    private static String yesString = "Y";

    public long blockGroupId;
    public String blockGroupName;
    public long blockGroupOrderNum;
    public long blockId;
    public long blockColumnNumber;
    public long blockRowNumber;
    public String blockName;
    public String blockDescription;

    public String blockShowNameFlag;
    public String blockShowNormFlag;
    public String blockShowFactFlag;
    public String blockShowSlFlag;
    public String blockHideCaptionFlag;
    public String blockZeroTargetFlag;
    public String blockDrillDownUrl;

    public long id;
    public String alias;
    public String description;
    public String barColor;
    public String normPrefix;
    public long orderNum;

    public String getBlockCode(){
        return "Section" + blockGroupId + "Block" + blockId;
    }
}
