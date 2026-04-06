package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {
    private Long id;
    private Long locationId;
    private Long eventId;
    private String userEmail;
    private String userName;
    private RateDTO rate;
    private String comment;
    private String createdAt;
}
