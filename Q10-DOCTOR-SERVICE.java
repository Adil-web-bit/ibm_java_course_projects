package com.healthcare.service;

import com.healthcare.entity.Doctor;
import com.healthcare.repository.DoctorRepository;
import com.healthcare.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service class for Doctor operations
 */
@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    /**
     * Method returns available time slots for doctor on a given date
     * 
     * @param doctor Doctor entity
     * @param date Given date
     * @return List of available time slots
     */
    public List<LocalTime> getAvailableTimeSlots(Doctor doctor, LocalDate date) {
        // Get doctor's available times
        List<LocalTime> availableTimes = doctor.getAvailableTimes();
        
        if (availableTimes == null) {
            availableTimes = generateDefaultTimeSlots();
        }
        
        // Get booked appointments for the doctor on the given date
        List<LocalTime> bookedSlots = appointmentRepository.findBookedTimeSlotsByDoctorAndDate(doctor, date);
        
        // Remove booked slots from available times
        List<LocalTime> availableSlots = new ArrayList<>();
        for (LocalTime time : availableTimes) {
            if (!bookedSlots.contains(time)) {
                availableSlots.add(time);
            }
        }
        
        return availableSlots;
    }

    /**
     * Method validates doctor login credentials and returns structured response
     * 
     * @param email Doctor's email
     * @param password Doctor's password
     * @return Structured response map
     */
    public Map<String, Object> validateDoctorLogin(String email, String password) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Find doctor by email
            Optional<Doctor> doctorOptional = doctorRepository.findByEmail(email);
            
            if (!doctorOptional.isPresent()) {
                response.put("success", false);
                response.put("message", "Doctor not found with email: " + email);
                return response;
            }
            
            Doctor doctor = doctorOptional.get();
            
            // Validate password (in real implementation, use password encoder)
            if (!validatePassword(password, doctor.getPassword())) {
                response.put("success", false);
                response.put("message", "Invalid credentials");
                return response;
            }
            
            // Successful validation
            response.put("success", true);
            response.put("message", "Login successful");
            response.put("doctorId", doctor.getDoctorId());
            response.put("specialization", doctor.getSpecialization());
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error during login validation: " + e.getMessage());
        }
        
        return response;
    }

    /**
     * Helper method to generate default time slots
     */
    private List<LocalTime> generateDefaultTimeSlots() {
        List<LocalTime> defaultSlots = new ArrayList<>();
        // Generate slots from 9 AM to 5 PM with 1-hour intervals
        for (int hour = 9; hour < 17; hour++) {
            defaultSlots.add(LocalTime.of(hour, 0));
        }
        return defaultSlots;
    }

    /**
     * Helper method to validate password
     */
    private boolean validatePassword(String rawPassword, String encodedPassword) {
        // In real implementation, use BCryptPasswordEncoder or similar
        return rawPassword != null && rawPassword.equals(encodedPassword);
    }
}