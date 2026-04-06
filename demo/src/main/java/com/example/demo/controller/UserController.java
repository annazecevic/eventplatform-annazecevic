package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest req) {
        try {
            userService.changePassword(req.getEmail(), req.getCurrentPassword(), req.getNewPassword());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> getUser(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest req) {
        try {
            User updated = userService.updateUserData(req.toUser());
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @Data
    public static class ChangePasswordRequest {
        private String email;
        private String currentPassword;
        private String newPassword;
    }

    @Data
    public static class UpdateUserRequest {
        private String email;
        private String name;
        private String phoneNumber;
        private String address;
        private String city;
        private java.time.LocalDate birthday;

        public User toUser() {
            return User.builder()
                .email(email)
                .name(name)
                .phoneNumber(phoneNumber)
                .address(address)
                .city(city)
                .birthday(birthday)
                .build();
        }
    }

    @Data
    public static class ErrorResponse {
        private final String message;
    }
}
