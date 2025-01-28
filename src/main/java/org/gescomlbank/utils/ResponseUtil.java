package org.gescomlbank.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {

    public static ResponseEntity<Map<String, Object>> successResponse(String message, Object data){
        Map<String, Object> response = new HashMap<>();

        response.put("status", HttpStatus.OK.value());
        response.put("success", true);
        response.put("message", message);
        response.put("data", data);

        return ResponseEntity.ok(response);
    }

    public static ResponseEntity<Map<String, Object>> errorsResponse(String message, HttpStatus status){
        Map<String, Object> response = new HashMap<>();

        response.put("status",status.value());
        response.put("success", false);
        response.put("message", message);
        response.put("data", null);

        return ResponseEntity.status(status).body(response);
    }
}
