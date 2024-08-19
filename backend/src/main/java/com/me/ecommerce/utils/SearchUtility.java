package com.me.ecommerce.utils;

public class SearchUtility {
    public static String[] tokenize(String input) {
        return input.trim().split("\\s+");
    }
}