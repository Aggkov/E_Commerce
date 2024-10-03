package com.me.ecommerce.service.impl.export;

import com.me.ecommerce.service.ExportService;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class YamlExportService<T> implements ExportService<T> {

    @Override
    public byte[] export(List<T> data) throws Exception {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            // Configure SnakeYAML to avoid adding type information
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK); // Block style for readability
            options.setPrettyFlow(true); // Make output human-readable
            options.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN); // Avoid !!tag for types

            // Create YAML instance with options
            Yaml yaml = new Yaml(options);
            yaml.dump(data, new OutputStreamWriter(out));

            return out.toByteArray();
        }
    }
}
