package ru.alfabank.dmpr.model.ctq;

public class CTQLayoutItem {
    private static String yesString = "Y";

    public int blockGroupId;
    public String blockGroupName;
    public int blockGroupOrderNum;
    public int blockId;
    public int blockColumnNumber;
    public int blockRowNumber;
    public String blockName;
    public String blockDescription;

    public String blockShowNameFlag;
    public String blockShowNormFlag;
    public String blockShowFactFlag;
    public String blockShowSlFlag;
    public String blockHideCaptionFlag;
    public String blockZeroTargetFlag;
    public String blockDrillDownUrl;

    public int id;
    public String alias;
    public String description;
    public String barColor;
    public String normPrefix;
    public int orderNum;

    public String getBlockCode(){
        return "Section" + blockGroupId + "Block" + blockId;
    }
}
