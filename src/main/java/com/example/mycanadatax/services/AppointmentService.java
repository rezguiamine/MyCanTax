package com.example.mycanadatax.services;

import com.example.mycanadatax.entities.Appointment;
import com.example.mycanadatax.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    // Define the available time slots


    private static final List<LocalTime> TIME_SLOTS = List.of(
            LocalTime.of(8, 0), LocalTime.of(9, 0), LocalTime.of(10, 0),
            LocalTime.of(11, 0), LocalTime.of(14, 0), LocalTime.of(15, 0),
            LocalTime.of(16, 0)
    );


    public List<LocalTime> getAvailableTimesOnDate(LocalDate date) {
        return appointmentRepository.findByAppointmentDate(date).stream()
                .map(Appointment::getAppointmentTime)
                .distinct() // Remove duplicate times
                .sorted()   // Sort the times in ascending order
                .collect(Collectors.toList());
    }

    public Appointment bookAppointment(String fullName, String email, String phoneNumber, LocalDate date, LocalTime time) {
        if (!TIME_SLOTS.contains(time)) {
            throw new IllegalArgumentException("Invalid time slot!");
        }

        if (appointmentRepository.existsByAppointmentDateAndAppointmentTime(date, time)) {
            throw new IllegalStateException("Time slot is already booked!");
        }

        Appointment appointment = new Appointment(fullName, email, phoneNumber, date, time);
        return appointmentRepository.save(appointment);
    }



    public List<LocalDate> getAvailableDates() {
        // Get all unique dates with appointments
        List<LocalDate> allDates = appointmentRepository.findAll().stream()
                .map(Appointment::getAppointmentDate)
                .filter(date -> !date.isBefore(LocalDate.now())) // Exclude past dates
                .distinct()
                .collect(Collectors.toList());

        // Check availability for each date
        List<LocalDate> availableDates = new ArrayList<>();
        for (LocalDate date : allDates) {
            List<LocalTime> bookedTimes = appointmentRepository.findByAppointmentDate(date).stream()
                    .map(Appointment::getAppointmentTime)
                    .collect(Collectors.toList());

            // If not all time slots are booked, consider the date available
            if (bookedTimes.containsAll(TIME_SLOTS)) {
                availableDates.add(date);
            }
        }

        return availableDates.stream().sorted().collect(Collectors.toList());
    }


    public Appointment createAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }


    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
    }


    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }


    public Appointment updateAppointment(Long id, Appointment appointment) {
        Appointment existingAppointment = getAppointmentById(id);
        existingAppointment.setFullName(appointment.getFullName());
        existingAppointment.setEmail(appointment.getEmail());
        existingAppointment.setPhoneNumber(appointment.getPhoneNumber());
        existingAppointment.setAppointmentDate(appointment.getAppointmentDate());
        existingAppointment.setAppointmentTime(appointment.getAppointmentTime());
        return appointmentRepository.save(existingAppointment);
    }


    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }


}
