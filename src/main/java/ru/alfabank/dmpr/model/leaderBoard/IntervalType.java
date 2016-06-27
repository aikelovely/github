package ru.alfabank.dmpr.model.leaderBoard;

/**
 * Период детализации: день, неделя, месяц, квартал или год. <br>
 * Строковые константы находятся в бд в справочнике PRDTYPE_LOV.
 */
public enum IntervalType {
    /**
     * День
     */
    Day("D"),
    /**
     * Неделя
     */
    Week("W"),
    /**
     * Месяц
     */
    Month("M"),
    /**
     * Квартал
     */
    Quarter("Q"),
    /**
     * Год
     */
    Year("Y");

    private final String name;

    private IntervalType(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return (otherName != null) && name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
