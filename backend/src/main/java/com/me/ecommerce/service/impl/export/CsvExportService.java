package com.me.ecommerce.service.impl.export;

import com.me.ecommerce.service.ExportService;
import com.opencsv.CSVWriter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class CsvExportService<T> implements ExportService<T> {

    @Override
    public byte[] export(List<T> data) throws Exception {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVWriter writer = new CSVWriter(new OutputStreamWriter(out))) {

            for (T item : data) {
                // Assuming `T` has a method `getFields()` that returns a list of fields
                List<String> fields = getFieldsFromItem(item);
                writer.writeNext(fields.toArray(new String[0]));
            }

            writer.flush();
            return out.toByteArray();
        }
    }

    private List<String> getFieldsFromItem(T item) {
        // Customize this method to extract the fields from object T
        return List.of(item.toString()); // Placeholder: Customize this
    }
}
