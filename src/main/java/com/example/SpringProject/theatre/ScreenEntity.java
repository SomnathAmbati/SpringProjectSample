package com.example.SpringProject.theatre;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "screen")
@Entity
public class ScreenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String screenName;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "theatre_id", nullable = false)
    private TheatreEntity theatre;
}
