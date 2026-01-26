package com.example.SpringProject.booking;

import com.example.SpringProject.user.User;

public interface BookingService {

    Booking createBooking(User user, BookingDTO dto);

    
}
