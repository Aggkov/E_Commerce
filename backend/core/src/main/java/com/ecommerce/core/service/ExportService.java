package com.ecommerce.core.service;

import java.util.List;

public interface ExportService<T> {

    /**
     * Method to export data to the desired format.
     * @param data The list of data to export.
     * @return A byte array representing the exported file content.
     * @throws Exception if there is an error during the export process.
     */
    byte[] export(List<T> data) throws Exception;
}
