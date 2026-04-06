package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class AccountRequestDTO {
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private LocalDate birthday;
    private String address;
    private String city;
}
