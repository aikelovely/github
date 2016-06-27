package ru.alfabank.dmpr.infrastructure.helper;

import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;

import java.util.Arrays;
import java.util.List;

public class ArrayHelper {
    public static <T> boolean AtLeastOneCommonElement(T[] a, T[] b){
        if (IsNullOrEmpty(a) || IsNullOrEmpty(b)) return false;
        List<T> bList = Arrays.asList(b);
        for (int i = 0; i < a.length; i++) {
            if (bList.contains(a[i])) return true;
        }
        return false;
    }

    public static <T> boolean IsNullOrEmpty(T[] a){
        return a == null || a.length == 0;
    }

    public static Double QuartileInc(Double[] a, int level){
        level = level < 0 ? 0 : level > 4 ? 4 : level;

        if (a.length == 0) return null;

        Double[] aSorted = LinqWrapper.from(a).sort(new Selector<Double, Double>() {
            @Override
            public Double select(Double count) {
                return count;
            }
        }).toArray(Double.class);

        double checkLine = 0.25*level*(a.length-1);
        int flooredCheckLine = (int)Math.floor(checkLine);

        return aSorted[flooredCheckLine] +
                ((aSorted.length > 1 ? aSorted[flooredCheckLine+1] : aSorted[flooredCheckLine]) - aSorted[flooredCheckLine])
                        *(checkLine - flooredCheckLine);
    }
}
