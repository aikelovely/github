package ru.alfabank.dmpr.infrastructure.helper.dev;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import ru.alfabank.dmpr.model.BaseOptions;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

public class MockXlsRepository {
    private String dataFolder = "/mock-data";
    private String extension = "xlsx";

    // write sync by locker
    private final Map<String, Object> cache = new HashMap<>();
    private final Object locker = new Object();
    private final String suffix = "Mapper";

    public <T> T read(Class mapperClass, Method method, Object[] args) {
        Class mapperInterface = getMapperInterface(mapperClass);
        String simpleName = mapperInterface.getSimpleName();
        String mapperName = simpleName.substring(0, simpleName.length() - suffix.length());

        @SuppressWarnings("unchecked")
        Class<T> returnType = (Class<T>) method.getReturnType();

        if (args != null && args.length == 1 && args[0] instanceof BaseOptions) {
            BaseOptions options = ((BaseOptions) args[0]);
            String widgetName = options.getWidgetName();

            T object = read(mapperName, widgetName, returnType);
            if (object != null)
                return object;
        }

        String methodName = method.getName().substring("get".length());
        T object = read(mapperName, methodName, returnType);
        if (object != null)
            return object;

        Class<?> componentType = returnType.getComponentType();

        @SuppressWarnings("unchecked")
        T result = (T) Array.newInstance(componentType, 0);
        return result;
    }

    private <T> T read(String folderName, String fileName, Class<T> returnType) {
        String keyName = folderName + "/" + fileName;

        Object object = cache.get(keyName);
        if (object == null) {

            String filePath = dataFolder + "/" + keyName + "." + extension;
            URL resource = getClass().getResource(filePath);
            if (resource == null)
                return null;

            try {
                synchronized (locker) {
                    try (InputStream stream = resource.openStream()) {
                        try (Workbook wb = WorkbookFactory.create(stream)) {
                            object = readObject(wb, returnType);
                            cache.put(keyName, object);
                        }
                    }
                }
            } catch (ReflectiveOperationException | InvalidFormatException | IOException e) {
                throw new RuntimeException(e);
            }
        }

        @SuppressWarnings("unchecked")
        T result = (T) object;

        return result;
    }

    private Class getMapperInterface(Class mapperClass) {
        if (mapperClass.isInterface())
            return mapperClass;

        Class[] interfaces = mapperClass.getInterfaces();
        if (interfaces.length != 1)
            throw new RuntimeException("interfaces.length != 1");

        if (!interfaces[0].getSimpleName().endsWith(suffix))
            throw new RuntimeException("!interfaces[0].getSimpleName().endsWith('Mapper')");

        return interfaces[0];
    }

    private <T> Object[] readObject(Workbook wb, Class<T> returnType)
            throws InstantiationException, IllegalAccessException {
        Sheet sheet = wb.getSheetAt(0);

        Map<String, Integer> columns = new HashMap<>();
        Row headerRow = sheet.getRow(0);
        for (Cell cell : headerRow) {
            String columnName = cell.getStringCellValue().replace("_", "").toLowerCase();
            columns.put(columnName, cell.getColumnIndex());
        }

        Class<?> componentType = returnType.getComponentType();
        Field[] fields = componentType.getFields();

        List<Row> rows = new ArrayList<>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;
            rows.add(row);
        }

        Object[] array = (Object[]) Array.newInstance(componentType, rows.size());
        for (Row row : rows) {
            Object item = componentType.newInstance();
            for (Field field : fields) {
                String columnName = field.getName().toLowerCase();
                Integer columnIndex = columns.get(columnName);
                if (columnIndex != null) {
                    try {
                        Cell cell = row.getCell(columnIndex);
                        if(cell != null){
                            setObjectValue(item, field, cell);
                        }
                    } catch (Exception ex) {
                        throw new RuntimeException("ColumnName: " + columnName, ex);
                    }
                }
            }

            array[row.getRowNum() - 1] = item;
        }
        return array;
    }


    private void setObjectValue(Object item, Field field, Cell cell) {
        Class<?> type = field.getType();
        String fieldName = field.getName();

        try {
            int cellType = cell.getCellType();
            switch (cellType) {
                case Cell.CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        if (type == LocalDate.class) field.set(item, new LocalDate(cell.getDateCellValue()));
                        else if(type == LocalDateTime.class) field.set(item, new LocalDateTime(cell.getDateCellValue()));
                    } else {
                        double value = cell.getNumericCellValue();
                        if (type == int.class) field.setInt(item, (int) value);
                        else if (type == long.class) field.setLong(item, (long) value);
                        else if (type == double.class) field.setDouble(item, value);
                        else if (type == Long.class) field.set(item, (long) value);
                        else if (type == Integer.class) field.set(item, (int) value);
                        else if (type == String.class) field.set(item, ((Double) value).toString());
                        else if(type == boolean.class) field.setBoolean(item, value == 1);
                        else field.set(item, value);
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    String value = cell.getStringCellValue().trim();
                    if (!value.equals(""))
                        field.set(item, value);
                    else
                        setEmptyValue(item, field);
                    break;
                case Cell.CELL_TYPE_BLANK:
                    setEmptyValue(item, field);
                    break;
                default:
                    throw new IllegalArgumentException("cellType " + cellType);
            }
        } catch (ReflectiveOperationException | IllegalStateException e) {
            throw new RuntimeException(field.getDeclaringClass() + "." + fieldName, e);
        }
    }

    private void setEmptyValue(Object item, Field field) throws IllegalAccessException {
        Class<?> type = field.getType();
        if (type == String.class) field.set(item, null);
        else if (type == int.class) field.setInt(item, 0);
        else if (type == long.class) field.setLong(item, 0);
        else if (type == double.class) field.setInt(item, 0);
        else if (type == Integer.class) field.set(item, null);
        else if (type == Long.class) field.set(item, null);
        else if (type == LocalDate.class) field.set(item, null);
        else
            throw new IllegalArgumentException(type.getName());
    }
}
