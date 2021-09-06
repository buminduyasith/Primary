package com.harini.primary.utill;

public class TextFormatter {
    public static String capitalizeFirstLetter(String text) {
        if (text != null || !text.isEmpty()) {
            return text.substring(0, 1).toUpperCase() + text.substring(1);
        }
        return null;
    }
}
