package com.fv.sec001.util;

public class ParseUtil {
    public static long parseLong(String value) {
        if (value == null || value.isBlank()) {
            return 0L;
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    public static double parseDouble(String value) {
        if (value == null || value.isBlank()) {
            return 0L;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }
}
