package com.healthcare.controller;

import com.healthcare.entity.Prescription;
import com.healthcare.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for Prescription operations
 */
@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    /**
     * POST endpoint saves a prescription with token and request body validation
     * 
     * @param prescription Prescription request body
     * @param request HTTP request for token validation
     * @return ResponseEntity with structured success or error messages
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> savePrescription(
            @Valid @RequestBody Prescription prescription, 
            HttpServletRequest request) {
        
        // Validate token
        String token = extractTokenFromRequest(request);
        if (!isValidToken(token)) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Invalid or missing authentication token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        try {
            // Save prescription
            Prescription savedPrescription = prescriptionService.savePrescription(prescription);
            
            // Return structured success message using ResponseEntity
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("success", true);
            successResponse.put("message", "Prescription saved successfully");
            successResponse.put("data", savedPrescription);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
            
        } catch (Exception e) {
            // Return structured error message using ResponseEntity
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error saving prescription: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Extracts authentication token from HTTP request
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return request.getParameter("token");
    }

    /**
     * Validates authentication token
     */
    private boolean isValidToken(String token) {
        return token != null && token.length() > 0;
    }
}