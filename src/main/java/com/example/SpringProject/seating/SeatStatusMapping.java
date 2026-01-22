package com.example.SpringProject.seating;

import com.example.SpringProject.common.AppEnums;
import com.example.SpringProject.theatre.ShowEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
@Data
@Table(name = "seat_status_mapping")
@Entity
public class SeatStatusMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "show_id", nullable = false)
    private ShowEntity show;

    @ManyToOne
    @JoinColumn(name = "seat_id", nullable = false)
    private SeatingEntity seat;

    @Enumerated(EnumType.STRING)
    private AppEnums.SeatStatus status;
}
