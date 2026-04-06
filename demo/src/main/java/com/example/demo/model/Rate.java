package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "rate", cascade = CascadeType.ALL, orphanRemoval = true)
    private Review review;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    @JsonIgnore
    private Location location;

    @ManyToOne
    @JoinColumn(name = "event_id")
    @JsonIgnore
    private Event event;

    @ManyToOne
    @JoinColumn(name = "user_email", nullable = false)
    private User user;

    private Integer performance;
    private Integer soundAndLighting;
    private Integer venue;
    private Integer overallImpression;

    @Column(length = 1000)
    private String comment;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
