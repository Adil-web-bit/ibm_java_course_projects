package com.healthcare.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

/**
 * JPA Entity representing a Doctor in the Healthcare Management System
 * 
 * This entity defines the structure for storing doctor information
 * with proper JPA annotations and validation constraints.
 * 
 * @author Healthcare Management System
 * @version 1.0
 */
@Entity
@Table(name = "doctors")
public class Doctor {

    // Primary Key with Auto-Generation Strategy
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private Long doctorId;

    // Foreign Key to Users table
    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId;

    // Medical License - Required and Unique
    @Column(name = "medical_license", unique = true, nullable = false, length = 50)
    @NotBlank(message = "Medical license is required")
    @Size(max = 50, message = "Medical license must not exceed 50 characters")
    private String medicalLicense;

    // Doctor's Specialization
    @Column(name = "specialization", nullable = false, length = 100)
    @NotBlank(message = "Specialization is required")
    @Size(max = 100, message = "Specialization must not exceed 100 characters")
    private String specialization;

    // Professional Qualification
    @Column(name = "qualification", length = 200)
    @Size(max = 200, message = "Qualification must not exceed 200 characters")
    private String qualification;

    // Years of Experience
    @Column(name = "years_experience")
    @Min(value = 0, message = "Years of experience cannot be negative")
    @Max(value = 50, message = "Years of experience cannot exceed 50")
    private Integer yearsExperience = 0;

    // Consultation Fee
    @Column(name = "consultation_fee", precision = 10, scale = 2)
    @DecimalMin(value = "0.0", inclusive = true, message = "Consultation fee cannot be negative")
    @Digits(integer = 8, fraction = 2, message = "Invalid consultation fee format")
    private BigDecimal consultationFee = BigDecimal.ZERO;

    // Department
    @Column(name = "department", length = 100)
    @Size(max = 100, message = "Department name must not exceed 100 characters")
    private String department;

    // Office Location
    @Column(name = "office_location", length = 100)
    @Size(max = 100, message = "Office location must not exceed 100 characters")
    private String officeLocation;

    // Available From Time
    @Column(name = "available_from")
    private LocalTime availableFrom = LocalTime.of(9, 0); // Default: 9:00 AM

    // Available To Time
    @Column(name = "available_to")
    private LocalTime availableTo = LocalTime.of(17, 0); // Default: 5:00 PM

    // Available Times - Collection of available time slots
    @ElementCollection
    @CollectionTable(
        name = "doctor_available_times",
        joinColumns = @JoinColumn(name = "doctor_id")
    )
    @Column(name = "available_time")
    private List<LocalTime> availableTimes;

    // Available Days of the Week
    @ElementCollection(targetClass = DayOfWeek.class)
    @CollectionTable(
        name = "doctor_available_days", 
        joinColumns = @JoinColumn(name = "doctor_id")
    )
    @Column(name = "day_of_week")
    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> availableDays;

    // Doctor's Biography
    @Column(name = "biography", columnDefinition = "TEXT")
    @Size(max = 2000, message = "Biography must not exceed 2000 characters")
    private String biography;

    // Availability Status
    @Column(name = "is_available")
    private Boolean isAvailable = true;

    // Audit Fields
    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date updatedAt;

    // Enum for Days of the Week
    public enum DayOfWeek {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    // Default Constructor
    public Doctor() {
        this.createdAt = new java.util.Date();
        this.updatedAt = new java.util.Date();
    }

    // Constructor with essential fields
    public Doctor(Long userId, String medicalLicense, String specialization) {
        this();
        this.userId = userId;
        this.medicalLicense = medicalLicense;
        this.specialization = specialization;
    }

    // JPA Lifecycle Callbacks
    @PrePersist
    protected void onCreate() {
        this.createdAt = new java.util.Date();
        this.updatedAt = new java.util.Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new java.util.Date();
    }

    // Getters and Setters

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMedicalLicense() {
        return medicalLicense;
    }

    public void setMedicalLicense(String medicalLicense) {
        this.medicalLicense = medicalLicense;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public Integer getYearsExperience() {
        return yearsExperience;
    }

    public void setYearsExperience(Integer yearsExperience) {
        this.yearsExperience = yearsExperience;
    }

    public BigDecimal getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(BigDecimal consultationFee) {
        this.consultationFee = consultationFee;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    public LocalTime getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(LocalTime availableFrom) {
        this.availableFrom = availableFrom;
    }

    public LocalTime getAvailableTo() {
        return availableTo;
    }

    public void setAvailableTo(LocalTime availableTo) {
        this.availableTo = availableTo;
    }

    public List<LocalTime> getAvailableTimes() {
        return availableTimes;
    }

    public void setAvailableTimes(List<LocalTime> availableTimes) {
        this.availableTimes = availableTimes;
    }

    public Set<DayOfWeek> getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(Set<DayOfWeek> availableDays) {
        this.availableDays = availableDays;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public java.util.Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.util.Date createdAt) {
        this.createdAt = createdAt;
    }

    public java.util.Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(java.util.Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Utility Methods

    /**
     * Checks if the doctor is available on a specific day
     * @param day the day to check
     * @return true if available, false otherwise
     */
    public boolean isAvailableOnDay(DayOfWeek day) {
        return availableDays != null && availableDays.contains(day);
    }

    /**
     * Adds an available time slot for the doctor
     * @param time the time slot to add
     */
    public void addAvailableTime(LocalTime time) {
        if (availableTimes != null) {
            availableTimes.add(time);
        }
    }

    /**
     * Removes an available time slot for the doctor
     * @param time the time slot to remove
     */
    public void removeAvailableTime(LocalTime time) {
        if (availableTimes != null) {
            availableTimes.remove(time);
        }
    }

    // toString Method
    @Override
    public String toString() {
        return "Doctor{" +
                "doctorId=" + doctorId +
                ", userId=" + userId +
                ", medicalLicense='" + medicalLicense + '\'' +
                ", specialization='" + specialization + '\'' +
                ", qualification='" + qualification + '\'' +
                ", yearsExperience=" + yearsExperience +
                ", consultationFee=" + consultationFee +
                ", department='" + department + '\'' +
                ", officeLocation='" + officeLocation + '\'' +
                ", availableFrom=" + availableFrom +
                ", availableTo=" + availableTo +
                ", isAvailable=" + isAvailable +
                '}';
    }

    // equals and hashCode methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doctor)) return false;
        Doctor doctor = (Doctor) o;
        return doctorId != null && doctorId.equals(doctor.doctorId);
    }

    @Override
    public int hashCode() {
        return doctorId != null ? doctorId.hashCode() : 0;
    }
}