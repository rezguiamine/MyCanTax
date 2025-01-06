package com.example.mycanadatax.controllers;

import com.example.mycanadatax.entities.Appointment;
import com.example.mycanadatax.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    // Get available dates
    @GetMapping("/available-dates")
    public ResponseEntity<List<LocalDate>> getAvailableDates() {
        return ResponseEntity.ok(appointmentService.getAvailableDates());
    }

    // Get available times for a specific date
    @GetMapping("/times")
    public List<LocalTime> getAvailableTimesOnDate(@RequestParam LocalDate date) {
        return appointmentService.getAvailableTimesOnDate(date);
    }

    // Book an appointment
    @PostMapping("/book")
    public ResponseEntity<Appointment> bookAppointment(@RequestParam String fullName,
                                                       @RequestParam String email,
                                                       @RequestParam String phoneNumber,
                                                       @RequestParam String date,
                                                       @RequestParam String time) {
        LocalDate localDate = LocalDate.parse(date);
        LocalTime localTime = LocalTime.parse(time);
        Appointment appointment = appointmentService.bookAppointment(fullName, email, phoneNumber, localDate, localTime);
        return ResponseEntity.ok(appointment);
    }
}
