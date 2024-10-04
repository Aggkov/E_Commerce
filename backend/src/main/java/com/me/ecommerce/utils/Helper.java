package com.me.ecommerce.utils;

import com.me.ecommerce.service.ExportService;
import com.me.ecommerce.service.impl.export.CsvExportService;
import com.me.ecommerce.service.impl.export.ExcelExportService;
import com.me.ecommerce.service.impl.export.JsonExportService;
import com.me.ecommerce.service.impl.export.YamlExportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class Helper<T> {

    public static <T> ExportService<T> getExportService(String type) {
        // Select the correct export service based on the type parameter
        return switch (type.toLowerCase()) {
            case "excel" -> new ExcelExportService<>();
//                break;
            case "csv" -> new CsvExportService<>();
//                break;
            case "json" -> new JsonExportService<>();
//                break;
            case "yaml" -> new YamlExportService<>();
//                break;
            default -> throw new IllegalArgumentException("Invalid export type");
        };
    }

    public static void setHeaders(HttpHeaders headers, String type) {
        // Set the content type and headers based on the file type
//        HttpHeaders headers = new HttpHeaders();
        switch (type.toLowerCase()) {
            case "excel":
                headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
                headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=export.xlsx");
                headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
                break;
            case "csv":
                headers.setContentType(MediaType.parseMediaType("text/csv"));
                headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=export.csv");
                headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
                break;
            case "json":
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=export.json");
                headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
                break;
            case "yaml":
                headers.setContentType(MediaType.parseMediaType("application/x-yml"));
                headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=export.yml");
                headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
                break;
        }
    }
}
