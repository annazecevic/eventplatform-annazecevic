package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.beans.factory.annotation.Autowired;
import jakarta.annotation.PostConstruct;
import com.example.demo.model.Event;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.LocationRepository;
import com.example.demo.model.Location;
import java.time.LocalDate;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ProjekatSiitMaliApplication {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private com.example.demo.repository.UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(ProjekatSiitMaliApplication.class, args);
    }

    @PostConstruct
    public void addTodayEvents() {
        LocalDate today = LocalDate.now();
        if (eventRepository.findByDate(today).isEmpty()) {
            eventRepository.save(Event.builder()
                    .name("Koncert Novi Sad")
                    .address("Trg Slobode 1")
                    .type("Muzika")
                    .date(today)
                    .price(1200.0)
                    .recurrent(false)
                    .location(null)
                    .build());
            eventRepository.save(Event.builder()
                    .name("Izložba umetnosti")
                    .address("Galerija Matice srpske")
                    .type("Izložba")
                    .date(today)
                    .price(0.0)
                    .recurrent(false)
                    .location(null)
                    .build());
            eventRepository.save(Event.builder()
                    .name("Filmska projekcija")
                    .address("Bioskop Arena")
                    .type("Film")
                    .date(today)
                    .price(500.0)
                    .recurrent(false)
                    .location(null)
                    .build());
        }
    }

    @PostConstruct
    public void addDefaultLocations() {
        if (locationRepository.count() == 0) {
            locationRepository.save(Location.builder()
                    .name("Trg Slobode")
                    .address("Trg Slobode 1")
                    .type("Javni prostor")
                    .description("Centralni gradski trg, mesto okupljanja i manifestacija.")
                    .createdAt(LocalDate.now())
                    .totalRating(0.0)
                    .build());
            locationRepository.save(Location.builder()
                    .name("Galerija Matice srpske")
                    .address("Trg galerija 1")
                    .type("Galerija")
                    .description("Najpoznatija umetnička galerija u Novom Sadu.")
                    .createdAt(LocalDate.now())
                    .totalRating(0.0)
                    .build());
            locationRepository.save(Location.builder()
                    .name("Bioskop Arena")
                    .address("Bulevar Mihajla Pupina 3")
                    .type("Bioskop")
                    .description("Savremeni bioskop sa više sala i bogatim programom.")
                    .createdAt(LocalDate.now())
                    .totalRating(0.0)
                    .build());
        }
    }

    @PostConstruct
    public void addDefaultUser() {
        if (!userRepository.existsByEmail("user@example.com")) {
            com.example.demo.model.User user = com.example.demo.model.User.builder()
                    .email("user@example.com")
                    .password(passwordEncoder.encode("password123"))
                    .name("Demo User")
                    .createdAt(LocalDate.now())
                    .phoneNumber("0601234567")
                    .birthday(LocalDate.of(1990, 1, 1))
                    .address("Ulica 1")
                    .city("Novi Sad")
                    .build();
            userRepository.save(user);
        }
    }
    @PostConstruct
    public void addDadmin() {
        userRepository.findByEmail("admin@example.com").ifPresent(existing -> {
            userRepository.delete(existing); // delete if it already exists
        });

        com.example.demo.model.Administrator admin = new com.example.demo.model.Administrator();
        admin.setEmail("admin@example.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setName("Admin");
        admin.setCreatedAt(LocalDate.now());
        admin.setPhoneNumber("0601234567");
        admin.setBirthday(LocalDate.of(1990, 1, 1));
        admin.setAddress("Ulica 1");
        admin.setCity("Novi Sad");

        userRepository.save(admin);
    }



}