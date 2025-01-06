package com.example.mycanadatax.repository;

import com.example.mycanadatax.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    // Custom query to find appointments for a specific date
    List<Appointment> findByAppointmentDate(LocalDate date);

    // Check if a time slot is already booked
    boolean existsByAppointmentDateAndAppointmentTime(LocalDate date, LocalTime time);
}
