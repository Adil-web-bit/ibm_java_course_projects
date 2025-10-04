# MySQL Database Schema Design for Healthcare Management System

## Database Overview
This schema supports a comprehensive healthcare management system with user authentication, appointment scheduling, patient records, and administrative functions.

---

## Table Structures

### 1. Users Table
**Purpose**: Central authentication and user management for all system users

```sql
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    user_type ENUM('DOCTOR', 'PATIENT', 'ADMIN') NOT NULL,
    status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL
);
```

**Key Features**:
- Primary Key: `user_id`
- Unique constraints on `username` and `email`
- Role-based access control via `user_type`
- Account status management
- Audit trail with timestamps

---

### 2. Doctors Table
**Purpose**: Extended profile information specific to medical practitioners

```sql
CREATE TABLE doctors (
    doctor_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT UNIQUE NOT NULL,
    medical_license VARCHAR(50) UNIQUE NOT NULL,
    specialization VARCHAR(100) NOT NULL,
    qualification VARCHAR(200),
    years_experience INT DEFAULT 0,
    consultation_fee DECIMAL(10,2) DEFAULT 0.00,
    department VARCHAR(100),
    office_location VARCHAR(100),
    available_from TIME DEFAULT '09:00:00',
    available_to TIME DEFAULT '17:00:00',
    available_days SET('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday') DEFAULT 'Monday,Tuesday,Wednesday,Thursday,Friday',
    biography TEXT,
    is_available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
);
```

**Key Features**:
- Foreign Key: `user_id` references `users(user_id)`
- Unique medical license tracking
- Availability scheduling with time and days
- Professional details and qualifications
- Consultation fee management

---

### 3. Patients Table
**Purpose**: Patient-specific information and medical details

```sql
CREATE TABLE patients (
    patient_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT UNIQUE NOT NULL,
    date_of_birth DATE NOT NULL,
    gender ENUM('MALE', 'FEMALE', 'OTHER') NOT NULL,
    blood_group ENUM('A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'),
    address TEXT,
    emergency_contact_name VARCHAR(100),
    emergency_contact_phone VARCHAR(20),
    emergency_contact_relation VARCHAR(50),
    medical_history TEXT,
    allergies TEXT,
    current_medications TEXT,
    insurance_provider VARCHAR(100),
    insurance_policy_number VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
);
```

**Key Features**:
- Foreign Key: `user_id` references `users(user_id)`
- Comprehensive medical information
- Emergency contact details
- Insurance information
- Medical history and allergy tracking

---

### 4. Appointments Table
**Purpose**: Manages all appointment bookings and scheduling

```sql
CREATE TABLE appointments (
    appointment_id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    duration_minutes INT DEFAULT 30,
    appointment_type ENUM('CONSULTATION', 'FOLLOW_UP', 'EMERGENCY', 'ROUTINE_CHECKUP') DEFAULT 'CONSULTATION',
    status ENUM('SCHEDULED', 'CONFIRMED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED', 'NO_SHOW') DEFAULT 'SCHEDULED',
    reason_for_visit TEXT,
    notes TEXT,
    prescription TEXT,
    diagnosis TEXT,
    next_appointment_recommended BOOLEAN DEFAULT FALSE,
    total_fee DECIMAL(10,2) DEFAULT 0.00,
    payment_status ENUM('PENDING', 'PAID', 'REFUNDED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    cancelled_at TIMESTAMP NULL,
    cancelled_by INT NULL,
    cancellation_reason TEXT,
    
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (cancelled_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
    
    UNIQUE KEY unique_appointment (doctor_id, appointment_date, appointment_time)
);
```

**Key Features**:
- Foreign Keys: `patient_id`, `doctor_id`, `cancelled_by`
- Prevents double-booking with unique constraint
- Complete appointment lifecycle management
- Payment tracking
- Cancellation audit trail

---

### 5. Medical Records Table
**Purpose**: Detailed medical records and treatment history

```sql
CREATE TABLE medical_records (
    record_id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    appointment_id INT,
    record_date DATE NOT NULL,
    record_type ENUM('CONSULTATION', 'LAB_RESULT', 'PRESCRIPTION', 'DIAGNOSIS', 'TREATMENT_PLAN') NOT NULL,
    chief_complaint TEXT,
    symptoms TEXT,
    physical_examination TEXT,
    diagnosis TEXT,
    treatment_plan TEXT,
    prescribed_medications TEXT,
    lab_tests_ordered TEXT,
    follow_up_instructions TEXT,
    doctor_notes TEXT,
    attachments_path VARCHAR(500),
    is_confidential BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id) ON DELETE SET NULL ON UPDATE CASCADE
);
```

**Key Features**:
- Foreign Keys: `patient_id`, `doctor_id`, `appointment_id`
- Comprehensive medical record keeping
- Support for various record types
- File attachment support
- Confidentiality flags

---

### 6. System Settings Table
**Purpose**: Application configuration and administrative settings

```sql
CREATE TABLE system_settings (
    setting_id INT PRIMARY KEY AUTO_INCREMENT,
    setting_key VARCHAR(100) UNIQUE NOT NULL,
    setting_value TEXT,
    setting_description VARCHAR(255),
    setting_type ENUM('STRING', 'INTEGER', 'BOOLEAN', 'JSON') DEFAULT 'STRING',
    is_editable BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by INT,
    
    FOREIGN KEY (updated_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE
);
```

**Key Features**:
- System-wide configuration management
- Audit trail for setting changes
- Type-safe setting values
- Administrative control

---

## Indexes for Performance Optimization

```sql
-- Performance indexes for frequently queried fields
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_type ON users(user_type);

CREATE INDEX idx_doctors_specialization ON doctors(specialization);
CREATE INDEX idx_doctors_available ON doctors(is_available);

CREATE INDEX idx_patients_dob ON patients(date_of_birth);

CREATE INDEX idx_appointments_date ON appointments(appointment_date);
CREATE INDEX idx_appointments_doctor_date ON appointments(doctor_id, appointment_date);
CREATE INDEX idx_appointments_patient ON appointments(patient_id);
CREATE INDEX idx_appointments_status ON appointments(status);

CREATE INDEX idx_medical_records_patient ON medical_records(patient_id);
CREATE INDEX idx_medical_records_date ON medical_records(record_date);
CREATE INDEX idx_medical_records_type ON medical_records(record_type);
```

---

## Sample Data Insertion

```sql
-- Insert sample admin user
INSERT INTO users (username, email, password_hash, first_name, last_name, user_type) 
VALUES ('admin', 'admin@hospital.com', '$2b$12$hash...', 'System', 'Administrator', 'ADMIN');

-- Insert sample doctor
INSERT INTO users (username, email, password_hash, first_name, last_name, phone, user_type) 
VALUES ('dr.smith', 'dr.smith@hospital.com', '$2b$12$hash...', 'John', 'Smith', '123-456-7890', 'DOCTOR');

INSERT INTO doctors (user_id, medical_license, specialization, qualification, years_experience, consultation_fee, department) 
VALUES (2, 'MD123456', 'Cardiology', 'MD in Cardiology, Harvard Medical School', 15, 150.00, 'Cardiology');

-- Insert sample patient
INSERT INTO users (username, email, password_hash, first_name, last_name, phone, user_type) 
VALUES ('patient1', 'patient1@email.com', '$2b$12$hash...', 'Jane', 'Doe', '098-765-4321', 'PATIENT');

INSERT INTO patients (user_id, date_of_birth, gender, blood_group, address, emergency_contact_name, emergency_contact_phone) 
VALUES (3, '1990-05-15', 'FEMALE', 'O+', '123 Main St, City, State', 'John Doe', '111-222-3333');
```

---

## Database Relationships Summary

1. **users** → **doctors** (One-to-One via user_id)
2. **users** → **patients** (One-to-One via user_id)
3. **doctors** → **appointments** (One-to-Many via doctor_id)
4. **patients** → **appointments** (One-to-Many via patient_id)
5. **patients** → **medical_records** (One-to-Many via patient_id)
6. **doctors** → **medical_records** (One-to-Many via doctor_id)
7. **appointments** → **medical_records** (One-to-Many via appointment_id)

---

## Key Database Features

✅ **6 Well-defined Tables** (exceeds requirement of 4)
✅ **Appropriate Field Names** - Self-descriptive and following naming conventions
✅ **Proper Data Types** - VARCHAR, INT, DECIMAL, ENUM, TEXT, TIMESTAMP, etc.
✅ **Foreign Key Relationships** - Maintaining referential integrity
✅ **Primary Keys** - Auto-incrementing integers for all tables
✅ **Unique Constraints** - Preventing duplicate entries where needed
✅ **Default Values** - Sensible defaults for optional fields
✅ **Cascading Actions** - Proper CASCADE and SET NULL behaviors
✅ **Performance Indexes** - Optimized for common queries
✅ **Audit Trails** - Created/updated timestamps throughout
