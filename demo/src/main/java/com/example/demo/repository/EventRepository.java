package com.example.demo.repository;

import com.example.demo.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByTypeContainingIgnoreCase(String type);
    List<Event> findByAddressContainingIgnoreCase(String address);
    List<Event> findByPriceLessThanEqual(Double price);
    List<Event> findByDate(LocalDate date);
    List<Event> findByLocationId(Long locationId);

    @Query("SELECT e FROM Event e WHERE (:name IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND (:type IS NULL OR LOWER(e.type) LIKE LOWER(CONCAT('%', :type, '%'))) AND (:address IS NULL OR LOWER(e.address) LIKE LOWER(CONCAT('%', :address, '%'))) AND (:maxPrice IS NULL OR e.price <= :maxPrice) AND (:date IS NULL OR e.date = :date)")
    List<Event> searchEvents(@Param("name") String name,
                            @Param("type") String type,
                            @Param("address") String address,
                            @Param("maxPrice") Double maxPrice,
                            @Param("date") java.time.LocalDate date);
}
