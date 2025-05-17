package com.immi.system.utils;

public class ValidationUtils {
    
    public static Long validateId(String id) throws IllegalArgumentException {
        try {
            return Long.valueOf(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid ID format");
        }
    }

    public static void validateDirection(String direction) {
        // Add validation for allowed directions if necessary
        if (!"Current".equals(direction) && !"Next".equals(direction) &&
            !"Previous".equals(direction) && !"First".equals(direction) &&
            !"Last".equals(direction)) {
            throw new IllegalArgumentException("Invalid direction value");
        }
    }
    
}
