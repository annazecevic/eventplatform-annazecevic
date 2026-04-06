package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    private String type;

    private LocalDate date;

    private Double price;

    private boolean recurrent;

    @ManyToOne
    @JoinColumn(name = "location_id")
    @JsonIgnore
    private Location location;
}