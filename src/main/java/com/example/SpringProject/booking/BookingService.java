package com.example.SpringProject.booking;

import com.example.SpringProject.Exception.BadRequestException;
import com.example.SpringProject.Exception.ConflictException;
import com.example.SpringProject.Exception.ResourceNotFoundException;
import com.example.SpringProject.user.User;

public interface BookingService {

    Booking createBooking(User user, BookingDTO dto) throws ResourceNotFoundException, BadRequestException, ConflictException;

    Booking getBookingById(Long bookingId) throws ResourceNotFoundException;

    
}
