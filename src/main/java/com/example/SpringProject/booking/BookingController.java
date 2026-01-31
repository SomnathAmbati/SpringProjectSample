package com.example.SpringProject.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SpringProject.Exception.ICinemaException;
import com.example.SpringProject.user.User;

import jakarta.validation.Valid;

import org.springframework.core.env.Environment;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/api/bookings")
@Validated
public class BookingController {

    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private Environment environment;

    /**
     * Create booking (INITIATED)
     * Seats are NOT booked here
     */
    @PostMapping
    public ResponseEntity<Booking> createBooking(@Valid @RequestBody BookingDTO bookingDTO) 
            throws ICinemaException {

        // 🔴 TEMP: user mocked (until auth is added)
        User dummyUser = new User();
        dummyUser.setId(1L); // replace with logged-in user later

        Booking booking = bookingService.createBooking(dummyUser, bookingDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }

    /**
     * Get booking details
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<Booking> getBooking(@PathVariable Long bookingId) throws com.example.SpringProject.Exception.ICinemaException {
        Booking booking = bookingService.getBookingById(bookingId);
        return ResponseEntity.ok(booking);
    }
}
