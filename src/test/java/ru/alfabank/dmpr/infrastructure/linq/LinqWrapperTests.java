package ru.alfabank.dmpr.infrastructure.linq;

import org.junit.Test;
import ru.alfabank.dmpr.model.BaseEntity;

import java.util.List;

import static org.junit.Assert.*;

public class LinqWrapperTests {

    @Test
    public void testDistinct() throws Exception {
        BaseEntity[] array = new BaseEntity[]{
                new BaseEntity(1, "blue"),
                new BaseEntity(4, "blue"),
                new BaseEntity(4, "red"),
                new BaseEntity(4, "red"),
                new BaseEntity(6, "green")
        };

        assertEquals(4, LinqWrapper.from(array).distinct().count());

        assertEquals(1, LinqWrapper.from(new BaseEntity[]{array[0]}).distinct().count());
        assertEquals(1, LinqWrapper.from(new BaseEntity[]{array[0], array[0], array[0]}).distinct().count());
        assertEquals(0, LinqWrapper.from(new BaseEntity[0]).distinct().count());
    }
}