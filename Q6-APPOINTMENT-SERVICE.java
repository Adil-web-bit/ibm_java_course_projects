package com.healthcare.service;

import com.healthcare.entity.Appointment;
import com.healthcare.entity.Doctor;
import com.healthcare.entity.Patient;
import com.healthcare.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for Appointment operations
 */
@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    /**
     * Booking method that saves an appointment
     * 
     * @param patient Patient entity
     * @param doctor Doctor entity
     * @param appointmentTime Appointment date and time
     * @return Saved appointment
     */
    public Appointment bookAppointment(Patient patient, Doctor doctor, LocalDateTime appointmentTime) {
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentTime(appointmentTime);
        
        return appointmentRepository.save(appointment);
    }

    /**
     * Method to retrieve appointments for a doctor on a specific date
     * 
     * @param doctor Doctor entity
     * @param date Specific date
     * @return List of appointments for the doctor on the specified date
     */
    public List<Appointment> getAppointmentsForDoctorOnDate(Doctor doctor, LocalDate date) {
        return appointmentRepository.findByDoctorAndAppointmentDate(doctor, date);
    }
}