package com.ecommerce.core.service.impl.export;

import com.ecommerce.core.utils.Reflection;
import com.ecommerce.core.service.ExportService;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelExportService<T> implements ExportService<T> {

    private final Reflection<T> reflectionHelper = new Reflection<>();

    @Override
    public byte[] export(List<T> data) throws Exception {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
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
                List<String> fields = reflectionHelper.getFieldsFromItem(item);
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
            if (reflectionHelper.getExcludedFields().contains(field.getName())) {
                continue;
            }
            headerRow.createCell(colIndex++).setCellValue(field.getName());
        }
    }
}
