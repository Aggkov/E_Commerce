package com.me.ecommerce.service.impl.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.me.ecommerce.service.ExportService;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class JsonExportService<T> implements ExportService<T> {

    private final ObjectMapper objectMapper;

    public JsonExportService() {
        this.objectMapper = new ObjectMapper();
        // Register the JavaTimeModule to handle Java 8 date/time types
        objectMapper.registerModule(new JavaTimeModule());
        // Optionally disable the feature WRITE_DATES_AS_TIMESTAMPS if you want readable date/time
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public byte[] export(List<T> data) throws Exception {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            objectMapper.writeValue(out, data);
            return out.toByteArray();
        }
    }
}
