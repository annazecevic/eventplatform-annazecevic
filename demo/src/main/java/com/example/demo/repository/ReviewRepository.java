package com.example.demo.repository;

import com.example.demo.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByLocationId(Long locationId);
    List<Review> findByUserEmail(String email);
    List<Review> findByEventId(Long eventId);
}

