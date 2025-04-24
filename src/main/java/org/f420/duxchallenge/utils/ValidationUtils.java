package org.f420.duxchallenge.utils;

public class ValidationUtils {
    public static boolean hasValidValue(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
