package com.immi.system.utils;

import java.util.Map;
import org.springframework.http.ResponseEntity;

public class CommonUtils {
    
    private ResponseEntity<Map<String, String>> validateAndConvertId(String id) {
        if (id == null || id.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "ID parameter is required"));
        }

        try {
            Long currentId = Long.valueOf(id);
            return ResponseEntity.ok(Map.of("currentId", currentId.toString())); // You can return the currentId if you need
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid ID format"));
        }
    }
    
}
