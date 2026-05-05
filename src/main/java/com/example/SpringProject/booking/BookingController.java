package com.example.SpringProject.booking;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SpringProject.Exception.ICinemaException;
import com.example.SpringProject.Exception.ResourceNotFoundException;
import com.example.SpringProject.user.User;
import com.example.SpringProject.user.UserRepository;

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
	private UserRepository userRepository;


	/**
	 * Create booking (INITIATED) Seats are NOT booked here
	 */
	@PostMapping
	public ResponseEntity<Booking> createBooking(@AuthenticationPrincipal UserDetails userDetails,
			@Valid @RequestBody BookingDTO bookingDTO) throws ICinemaException {

		// 🔐 Get logged-in user from DB
		User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(()->new ICinemaException("User Not Found"));

		Booking booking = bookingService.createBooking(user, bookingDTO);

		return ResponseEntity.status(HttpStatus.CREATED).body(booking);
	}

	/**
	 * Get booking details
	 */
	@GetMapping("/{bookingId}")
	public ResponseEntity<Booking> getBooking(@PathVariable Long bookingId) throws ICinemaException {
		Booking booking = bookingService.getBookingById(bookingId);
		return ResponseEntity.ok(booking);
	}

	// BOOKING HISTORY (TEMP USER ID)
	@GetMapping("/user/{userId}")
	public List<Booking> getUserBookings(@PathVariable Long userId) throws ResourceNotFoundException {

		return bookingService.getBookingsByUserId(userId);
	}

	@GetMapping("/my")
	public List<Booking> myBookings(@AuthenticationPrincipal UserDetails userDetails) throws ResourceNotFoundException {
		User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();

		return bookingService.getBookingsByUserId(user.getId());
	}

}
