package com.me.ecommerce.service.impl.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.me.ecommerce.service.ExportService;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class JsonExportService<T> implements ExportService<T> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] export(List<T> data) throws Exception {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            objectMapper.writeValue(out, data);
            return out.toByteArray();
        }
    }
}
