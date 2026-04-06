package com.example.demo.service;

import com.example.demo.dto.RateDTO;
import com.example.demo.dto.ReviewDTO;
import com.example.demo.model.Event;
import com.example.demo.model.Location;
import com.example.demo.model.Rate;
import com.example.demo.model.Review;
import com.example.demo.model.User;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.LocationRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final LocationRepository locationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        Location location = locationRepository.findById(reviewDTO.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found"));

        User user = userRepository.findByEmail(reviewDTO.getUserEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Event event = null;
        if (reviewDTO.getEventId() != null) {
            event = eventRepository.findById(reviewDTO.getEventId())
                    .orElseThrow(() -> new RuntimeException("Event not found"));
        }

        // Create Rate object from DTO
        Rate rate = Rate.builder()
                .location(location)
                .event(event)
                .user(user)
                .performance(reviewDTO.getRate().getPerformance())
                .soundAndLighting(reviewDTO.getRate().getSoundAndLighting())
                .venue(reviewDTO.getRate().getVenue())
                .overallImpression(reviewDTO.getRate().getOverallImpression())
                .comment(reviewDTO.getComment())
                .build();

        Review review = Review.builder()
                .location(location)
                .event(event)
                .user(user)
                .rate(rate)
                .comment(reviewDTO.getComment())
                .build();

        Review saved = reviewRepository.save(review);

        // Update location total rating
        updateLocationRating(location.getId());

        return convertToDTO(saved);
    }

    public List<ReviewDTO> getReviewsByLocation(Long locationId) {
        return reviewRepository.findByLocationId(locationId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReviewDTO> getReviewsByUser(String email) {
        return reviewRepository.findByUserEmail(email).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private void updateLocationRating(Long locationId) {
        List<Review> reviews = reviewRepository.findByLocationId(locationId);
        if (reviews.isEmpty()) return;

        double avgRating = reviews.stream()
                .filter(r -> r.getRate() != null && r.getRate().getOverallImpression() != null)
                .mapToInt(r -> r.getRate().getOverallImpression())
                .average()
                .orElse(0.0);

        Location location = locationRepository.findById(locationId).orElseThrow();
        location.setTotalRating(avgRating);
        locationRepository.save(location);
    }

    private ReviewDTO convertToDTO(Review review) {
        RateDTO rateDTO = null;
        if (review.getRate() != null) {
            rateDTO = RateDTO.builder()
                    .id(review.getRate().getId())
                    .performance(review.getRate().getPerformance())
                    .soundAndLighting(review.getRate().getSoundAndLighting())
                    .venue(review.getRate().getVenue())
                    .overallImpression(review.getRate().getOverallImpression())
                    .build();
        }

        return ReviewDTO.builder()
                .id(review.getId())
                .locationId(review.getLocation().getId())
                .eventId(review.getEvent() != null ? review.getEvent().getId() : null)
                .userEmail(review.getUser().getEmail())
                .userName(review.getUser().getName())
                .rate(rateDTO)
                .comment(review.getComment())
                .createdAt(review.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();
    }
}
