package com.ecommerce.core.service.impl.export;

import com.ecommerce.core.utils.Reflection;
import com.ecommerce.core.service.ExportService;
import com.opencsv.CSVWriter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvExportService<T> implements ExportService<T> {

    private final Reflection<T> reflectionHelper = new Reflection<>();

    @Override
    public byte[] export(List<T> data) throws Exception {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVWriter writer = new CSVWriter(new OutputStreamWriter(out), ';',
                     CSVWriter.DEFAULT_QUOTE_CHARACTER,
                     CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                     CSVWriter.DEFAULT_LINE_END)) {

            List<String> headers = getHeaders(data.get(0));
            writer.writeNext(headers.toArray(new String[0]));
            int counter = 0;
            for (T item : data) {
                // Assuming `T` has a method `getFields()` that returns a list of fields
                List<String> fields = reflectionHelper.getFieldsFromItem(item);
                writer.writeNext(new String[]{String.valueOf(++counter), Arrays.toString(fields.toArray())});
            }
            writer.flush();
            return out.toByteArray();
        }
    }

    private List<String> getHeaders(T item) {
        List<String> headers = new ArrayList<>();
        Class<?> clazz = item.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            // Skip the fields you don't want in the export (like imageUrl)
            if (reflectionHelper.getExcludedFields().contains(field.getName())) {
                continue;
            }
            headers.add(field.getName());
        }
        return headers;
    }

//    private List<String> getFieldsFromItem(T item) {
//        // implement reflection or use a library to do this.
//        List<String> fieldValues = new ArrayList<>();
//
//        Class<?> clazz = item.getClass();
//        for (Field field : clazz.getDeclaredFields()) {
//            if (EXCLUDED_FIELDS.contains(field.getName())) {
//                continue;
//            }
//            // Make private fields accessible
//            field.setAccessible(true);
//            try {
//                // Get the field value for the current object
//                Object value = field.get(item);
//                // Convert the field value to string and add to list (handling null values)
//                fieldValues.add(value != null ? value.toString() : "null");
//            } catch (IllegalAccessException e) {
//                // Handle potential errors when accessing the field
//                fieldValues.add("Error: Unable to access field");
//            }
//        }
//        return fieldValues;
//    }
}
