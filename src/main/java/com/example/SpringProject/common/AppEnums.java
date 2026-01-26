package com.example.SpringProject.common;

public interface AppEnums {
    
    enum RoleType { ADMIN, USER }

    enum SeatType { REGULAR, PREMIUM, RECLINER }

    enum SeatStatus { AVAILABLE, SELECTED, BOOKED }

    enum BookingStatus { INITIATED, CONFIRMED, CANCELLED }

    enum PaymentMode { CREDIT_CARD, DEBIT_CARD }

    enum PaymentStatus { SUCCESS, FAILED }

    // enum GenreType { ACTION, COMEDY, DRAMA, HORROR, THRILLER }

    // enum LanguageType { ENGLISH, HINDI, TELUGU, TAMIL }

    enum CensorRating { U, UA, A, S } // U: Universal, UA: Parental guidance, A: Adult, S: Special
}