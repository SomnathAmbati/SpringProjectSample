package com.example.SpringProject.seating;

import com.example.SpringProject.common.AppEnums;
import com.example.SpringProject.theatre.ShowEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Entity
@Table(
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"show_id", "seat_type"}
    )
)
@Data
public class ShowSeatPriceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "show_id", nullable = false)
    private ShowEntity show;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_type", nullable = false)
    private AppEnums.SeatType seatType;

    private Double price;
}
