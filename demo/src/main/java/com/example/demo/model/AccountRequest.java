package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AccountRequest {

    @Id
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private LocalDate createdAt;

    private String rejectionReason;
}