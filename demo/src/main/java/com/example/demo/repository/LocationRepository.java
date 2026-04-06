package com.example.demo.repository;

import com.example.demo.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByNameContainingIgnoreCase(String name);
    List<Location> findByAddressContainingIgnoreCase(String address);
    List<Location> findByTypeContainingIgnoreCase(String type);

    @org.springframework.data.jpa.repository.Query("SELECT l FROM Location l WHERE (:name IS NULL OR LOWER(l.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND (:address IS NULL OR LOWER(l.address) LIKE LOWER(CONCAT('%', :address, '%'))) AND (:type IS NULL OR LOWER(l.type) LIKE LOWER(CONCAT('%', :type, '%')))")
    java.util.List<Location> searchLocations(@org.springframework.data.repository.query.Param("name") String name,
                                            @org.springframework.data.repository.query.Param("address") String address,
                                            @org.springframework.data.repository.query.Param("type") String type);
}
