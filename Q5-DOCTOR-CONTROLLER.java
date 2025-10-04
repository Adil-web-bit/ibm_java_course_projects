package com.healthcare.controller;

import com.healthcare.entity.Doctor;
import com.healthcare.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Doctor availability
 */
@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    /**
     * GET endpoint for doctor availability using dynamic parameters
     */
    @GetMapping("/availability")
    public ResponseEntity<Map<String, Object>> getDoctorAvailability(
            @RequestParam(value = "specialization", required = false) String specialization,
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "time", required = false) String time,
            HttpServletRequest request) {
        
        // Validate token
        String token = extractTokenFromRequest(request);
        if (!isValidToken(token)) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Invalid token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        // Get available doctors based on dynamic parameters
        List<Doctor> availableDoctors = doctorService.findAvailableDoctors(specialization, date, time);

        // Create structured response using ResponseEntity
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", availableDoctors);
        response.put("count", availableDoctors.size());

        return ResponseEntity.ok(response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return request.getParameter("token");
    }

    private boolean isValidToken(String token) {
        return token != null && token.length() > 0;
    }
}