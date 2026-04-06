package com.example.demo.service;

import com.example.demo.model.AccountRequest;
import com.example.demo.model.RequestStatus;
import com.example.demo.model.User;
import com.example.demo.repository.AccountRequestRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountRequestService {

    private final AccountRequestRepository accountRequestRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public AccountRequest createRequest(String email, String rawPassword, String address) {
        if (accountRequestRepository.existsById(email) || userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already used");
        }

        AccountRequest request = new AccountRequest();
        request.setEmail(email);
        request.setPassword(passwordEncoder.encode(rawPassword));
        request.setAddress(address);
        request.setStatus(RequestStatus.PENDING);
        request.setCreatedAt(LocalDate.now());

        return accountRequestRepository.save(request);
    }

    public List<AccountRequest> getAllRequests() {
        return accountRequestRepository.findAll();
    }

    public Optional<AccountRequest> getRequest(String email) {
        return accountRequestRepository.findById(email);
    }

    // ✅ Admin accepts request and creates User
    public void acceptRequest(String email) {
        AccountRequest request = accountRequestRepository.findById(email)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus(RequestStatus.APPROVED);
        accountRequestRepository.save(request);

        User user = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .name("User") // or default name if you want
                .address(request.getAddress())
                .city("N/A")  // or leave blank / null if your entity allows it
                .createdAt(LocalDate.now())
                .build();

        userRepository.save(user);

        // 📧 Send approval email
        emailService.send(
                request.getEmail(),
                "Registration Approved",
                "Dear user,\n\nYour account has been approved. You can now log in at http://localhost:4200/login.\n\nBest regards,\nAdmin"
        );
    }


    // ❌ Admin rejects
    public void rejectRequest(String email) {
        AccountRequest request = accountRequestRepository.findById(email)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus(RequestStatus.REJECTED);
        request.setRejectionReason("Rejected by admin");
        accountRequestRepository.save(request);

        // 📧 Send rejection email
        emailService.send(
                request.getEmail(),
                "Registration Rejected",
                "Dear user,\n\nYour registration request has been rejected by the administrator.\n\nBest regards,\nAdmin"
        );
    }

}
