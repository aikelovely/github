package ru.alfabank.dmpr.filter.ctq;

import ru.alfabank.dmpr.infrastructure.linq.Group;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.ctq.CTQLayoutItem;

public class CTQFilterHelper {
    public static CTQLayoutItem[] getSections(LinqWrapper<CTQLayoutItem> list){
        return list.group(new Selector<CTQLayoutItem, Object>() {
            @Override
            public Long select(CTQLayoutItem ctqLayoutItem) {
                return ctqLayoutItem.blockGroupId;
            }
        }).select(new Selector<Group<Object,CTQLayoutItem>, CTQLayoutItem>() {
            @Override
            public CTQLayoutItem select(Group<Object, CTQLayoutItem> ctqLayoutItems) {
                return ctqLayoutItems.getItems().first();
            }
        }).sort(new Selector<CTQLayoutItem, Long>() {
            @Override
            public Long select(CTQLayoutItem ctqLayoutItem) {
                return ctqLayoutItem.blockGroupOrderNum;
            }
        }).toArray(CTQLayoutItem.class);
    }

    public static LinqWrapper<CTQLayoutItem> getBlocksBySection(LinqWrapper<CTQLayoutItem> blocks, final long neededSection){
        return blocks.filter(new Predicate<CTQLayoutItem>() {
            @Override
            public boolean check(CTQLayoutItem item) {
                return item.blockGroupId == neededSection;
            }
        }).group(new Selector<CTQLayoutItem, Long>() {
            @Override
            public Long select(CTQLayoutItem ctqLayoutItem) {
                return ctqLayoutItem.blockId;
            }
        }).select(new Selector<Group<Long, CTQLayoutItem>, CTQLayoutItem>() {
            @Override
            public CTQLayoutItem select(Group<Long, CTQLayoutItem> ctqLayoutItems) {
                return ctqLayoutItems.getItems().first();
            }
        });
    }

    public static CTQLayoutItem[] getBlocksByColumn(LinqWrapper<CTQLayoutItem> blocks, final long neededColumn){
        return blocks.filter(new Predicate<CTQLayoutItem>() {
            @Override
            public boolean check(CTQLayoutItem item) {
                return item.blockColumnNumber == neededColumn;
            }
        }).sort(new Selector<CTQLayoutItem, Long>() {
            @Override
            public Long select(CTQLayoutItem ctqLayoutItem) {
                return ctqLayoutItem.blockRowNumber;
            }
        }).toArray(CTQLayoutItem.class);
    }

    public static CTQLayoutItem[] getBlockByIdAndGroupId(LinqWrapper<CTQLayoutItem> layoutWrapper, final String blockGroupId, final String blockId){
        return layoutWrapper.filter(new Predicate<CTQLayoutItem>() {
            @Override
            public boolean check(CTQLayoutItem item) {
                return item.blockGroupId == Long.parseLong(blockGroupId) && item.blockId == Long.parseLong(blockId);
            }
        }).toArray(CTQLayoutItem.class);
    }
}
