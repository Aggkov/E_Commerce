package com.me.ecommerce.utils;

public class SearchUtility {
    public static String[] tokenizeWords(String input) {
        return input.trim().split("\\s+");
    }

    public static char[] tokenizeCharacters(String input) {
        return input.trim().toCharArray();
    }
}