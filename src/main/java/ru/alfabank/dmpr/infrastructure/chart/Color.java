package ru.alfabank.dmpr.infrastructure.chart;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.util.HashMap;
import java.util.Map;

/**
 * Структура для представления цвета RGB
 */
public class Color {
    private final static Map<String, Color> cache = new HashMap<>();
    // Статические поля для наиболее популярных цветов
    public final static Color RedColor = new Color(253, 166, 162);
    public final static Color DarkRedColor = new Color(175, 57, 57);
    public final static Color GreenColor = new Color(195, 214, 155);
    public final static Color BlueColor = new Color(124, 181, 236);
    public final static Color DarkBlueColor = new Color(51, 122, 183);
    public final static Color LightBlueColor = new Color(79, 129, 189);
    public final static Color GreyColor = new Color(160, 160, 160);
    public final static Color OrangeColor = new Color(240, 180, 80);
    public final static Color DarkOrangeColor = new Color(206, 133, 0);
    public final static Color WhiteColor = new Color(255, 255, 255);
    public final static Color DarkGreenColor = new Color(23, 148, 49);
    public final static Color SuperRedColor = new Color(248, 0, 0);

    static {
        RedColor.register();
        GreenColor.register();
        BlueColor.register();
        GreyColor.register();
    }

    private void register() {
        cache.put(toString(), this);
    }

    public final byte[] bytes = new byte[3];

    /**
     * Приватный конструктор
     *
     * @param red   компонент цвета
     * @param green компонент цвета
     * @param blue  компонент цвета
     */
    private Color(int red, int green, int blue) {
        bytes[0] = (byte) red;
        bytes[1] = (byte) green;
        bytes[2] = (byte) blue;
    }

    /**
     * Фабричный метод, возвращающий экземпляр на основании значений сосотавных компонент цвета
     *
     * @param red   компонент цвета
     * @param green компонент цвета
     * @param blue  компонент цвета
     * @return Color
     */
    public static Color valueOf(int red, int green, int blue) {
        return valueOf(toString(new byte[]{(byte) red, (byte) green, (byte) blue}));
    }

    /**
     * Фабричный метод, возвращающий экземпляр на основании Hex представления в формате #FFFFFF
     *
     * @param hexString - Hex представление
     * @return Color
     */
    public static Color valueOf(String hexString) {
        Color color = cache.get(hexString);
        if (color == null) {
            byte[] bytes = toBytes(hexString);
            cache.put(hexString, color = new Color(bytes[0], bytes[1], bytes[2]));
        }
        return color;
    }

    /**
     * @return Hex представление значений RGB в формате #FFFFFF
     */
    @Override
    public String toString() {
        return toString(bytes);
    }

    private static String toString(byte[] bytes) {
        return "#" + Hex.encodeHexString(bytes).toUpperCase();
    }

    private static byte[] toBytes(String hexString) {
        try {
            return Hex.decodeHex(hexString.substring(1).toCharArray());
        } catch (DecoderException e) {
            throw new RuntimeException();
        }
    }
}
