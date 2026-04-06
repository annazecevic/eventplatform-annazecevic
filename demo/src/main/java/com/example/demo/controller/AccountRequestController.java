package com.example.demo.controller;

import com.example.demo.dto.AccountRequestDTO;
import com.example.demo.model.AccountRequest;
import com.example.demo.service.AccountRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/account-requests")
@RequiredArgsConstructor
public class AccountRequestController {

    private final AccountRequestService accountRequestService;

    @PostMapping
    public AccountRequest createRequest(@RequestBody AccountRequestDTO dto) {
        return accountRequestService.createRequest(dto.getEmail(), dto.getPassword(), dto.getAddress());
    }


    @GetMapping
    public List<AccountRequest> getAllRequests() {
        return accountRequestService.getAllRequests();
    }

    @PostMapping("/{email}/accept")
    public void acceptRequest(@PathVariable String email) {
        accountRequestService.acceptRequest(email);
    }


    @PostMapping("/{email}/reject")
    public void rejectRequest(@PathVariable String email) {
        accountRequestService.rejectRequest(email);
    }

}
