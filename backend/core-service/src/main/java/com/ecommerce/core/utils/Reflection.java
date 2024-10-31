package com.ecommerce.core.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Reflection<T> {

    private final Set<String> EXCLUDED_FIELDS = Set.of("serialVersionUID");

    // or use library
    public List<String> getFieldsFromItem(T item) {
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
                // Convert the field value to string and add to list (handling null values)
                fieldValues.add(value != null ? value.toString() : "null");
            } catch (IllegalAccessException e) {
                fieldValues.add("Error: Unable to access field");
            }
        }
        return fieldValues;
    }

    public Set<String> getExcludedFields() {
        return EXCLUDED_FIELDS;
    }



    // EXTRACT NESTED OBJECTS FIELDS NON-RECURSIVE
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
//
//                // If the field is a nested object, handle it separately (e.g., ProductCategoryDTO)
//               if (value != null && field.getType().getSimpleName().equals("ProductCategoryDTO")) {
//                    // Extract the category name specifically
//                    Field categoryNameField = value.getClass().getDeclaredField("categoryName");
//                    categoryNameField.setAccessible(true);
//                   Object categoryNameValue = categoryNameField.get(value);
//                    fieldValues.add(categoryNameValue != null ? categoryNameValue.toString() : "null");
//                } else {
//
//                // Convert the field value to string and add to list (handling null values)
//                fieldValues.add(value != null ? value.toString() : "null");
//               }
//            } catch (IllegalAccessException e) {
//                // Handle potential errors when accessing the field
//                fieldValues.add("Error: Unable to access field");
//            }
//        }
//        return fieldValues;
//    }

    // OR WITH RECURSION
//    public List<String> getFieldsFromItem(T item) {
//        List<String> fieldValues = new ArrayList<>();
//
//        // Get the class of the object
//        Class<?> clazz = item.getClass();
//
//        // Loop through all fields of the class
//        for (Field field : clazz.getDeclaredFields()) {
//            // Skip excluded fields
//            if (EXCLUDED_FIELDS.contains(field.getName())) {
//                continue;
//            }
//
//            // Make private fields accessible
//            field.setAccessible(true);
//
//            try {
//                Object value = field.get(item);
//
//                // Handle nested objects generically (e.g., ProductCategoryDTO)
//                if (value != null && !isPrimitiveOrWrapper(value.getClass())) {
//                    fieldValues.addAll(getFieldsFromItem(value)); // Recursive call for nested objects
//                } else {
//                    fieldValues.add(value != null ? value.toString() : "null"); // Handle normal fields
//                }
//
//            } catch (IllegalAccessException e) {
//                fieldValues.add("Error: Unable to access field");
//            }
//        }
//        return fieldValues;
//    }
//
//    // Helper method to check if a field is primitive, wrapper, or String (i.e., not a nested object)
//    private boolean isPrimitiveOrWrapper(Class<?> clazz) {
//        return clazz.isPrimitive() ||
//                clazz == String.class ||
//                clazz == Integer.class ||
//                clazz == Long.class ||
//                clazz == Boolean.class ||
//                clazz == Byte.class ||
//                clazz == Short.class ||
//                clazz == Double.class ||
//                clazz == Float.class ||
//                clazz == Character.class;
//    }
}
