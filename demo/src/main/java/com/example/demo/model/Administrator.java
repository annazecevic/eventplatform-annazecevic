package com.example.demo.model;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
public class Administrator extends User {
    // no extra fields needed
}
