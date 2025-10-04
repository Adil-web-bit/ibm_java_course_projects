package com.healthcare.repository;

import com.healthcare.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Patient entity operations
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * Method retrieves patient by email using derived query
     * 
     * @param email Patient's email
     * @return Optional Patient
     */
    Optional<Patient> findByEmail(String email);

    /**
     * Method retrieves patient using either email or phone number
     * Custom query implementation
     * 
     * @param email Patient's email
     * @param phone Patient's phone number
     * @return Optional Patient
     */
    @Query("SELECT p FROM Patient p WHERE p.email = :email OR p.phone = :phone")
    Optional<Patient> findByEmailOrPhone(@Param("email") String email, @Param("phone") String phone);
}