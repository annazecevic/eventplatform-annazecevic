package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RateDTO {
    private Long id;
    private Integer performance;
    private Integer soundAndLighting;
    private Integer venue;
    private Integer overallImpression;
}

