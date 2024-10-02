package com.me.ecommerce.service.impl.export;

import com.me.ecommerce.service.ExportService;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelExportService<T> implements ExportService<T> {

    private static final Set<String> EXCLUDED_FIELDS = Set.of("imageUrl", "serialVersionUID");

    @Override
    public byte[] export(List<T> data) throws Exception {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Data");
            // Create the header row (Row 0)
            if (!data.isEmpty()) {
                createHeaderRow(sheet, data.get(0));
            }
            // Assuming that T has a method to return field values as a list or array
            for (int i = 0; i < data.size(); i++) {
                Row row = sheet.createRow(i + 1);
                T item = data.get(i);

                // Assuming `T` has a method `getFields()` that returns the object's fields as a list of strings
                List<String> fields = getFieldsFromItem(item);
                for (int j = 0; j < fields.size(); j++) {
                    row.createCell(j).setCellValue(fields.get(j));
                }
            }
            workbook.write(out);
            return out.toByteArray();
        }
    }

    // Helper method to create the header row based on field names
    private void createHeaderRow(Sheet sheet, T firstItem) {
        Row headerRow = sheet.createRow(0); // Create header row at index 0

        // Use reflection to get the fields from the ProductDTO class
        Class<?> clazz = firstItem.getClass();
        Field[] fields = clazz.getDeclaredFields();

        int colIndex = 0;
        for (Field field : fields) {
            // Skip the fields you don't want in the export (like imageUrl)
            if ("imageUrl".equals(field.getName()) || "serialVersionUID".equals(field.getName())) {
                continue;
            }
            headerRow.createCell(colIndex++).setCellValue(field.getName());
        }
    }

    private List<String> getFieldsFromItem(T item) {
        // implement reflection or use a library to do this.
        List<String> fieldValues = new ArrayList<>();

        Class<?> clazz = item.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (EXCLUDED_FIELDS.contains(field.getName())) {
                continue;
            }
            // Make private fields accessible
            field.setAccessible(true);
            try {
                // Get the field value for the current object
                Object value = field.get(item);

                // If the field is a nested object, handle it separately (e.g., ProductCategoryDTO)
//                if (value != null && field.getType().getSimpleName().equals("ProductCategoryDTO")) {
//                    // Extract the category name specifically
//                    Field categoryNameField = value.getClass().getDeclaredField("categoryName");
//                    categoryNameField.setAccessible(true);
//                    Object categoryNameValue = categoryNameField.get(value);
//                    fieldValues.add(categoryNameValue != null ? categoryNameValue.toString() : "null");
//                } else {

                    // Convert the field value to string and add to list (handling null values)
                    fieldValues.add(value != null ? value.toString() : "null");
//                }
            } catch (IllegalAccessException e) {
                // Handle potential errors when accessing the field
                fieldValues.add("Error: Unable to access field");
            }
        }
        return fieldValues;
    }
}
