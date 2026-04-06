package com.example.demo.service;

import com.example.demo.model.Location;
import com.example.demo.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public java.util.List<Location> searchLocations(String name, String address, String type) {
        return locationRepository.searchLocations(name, address, type);

    }

    public Location getLocationById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found"));
    }

    public Location createLocation(Location location) {
        return locationRepository.save(location);
    }

    public Location updateLocation(Long id, Location updatedLocation) {
        Location loc = getLocationById(id);
        loc.setName(updatedLocation.getName());
        loc.setAddress(updatedLocation.getAddress());
        loc.setType(updatedLocation.getType());
        loc.setDescription(updatedLocation.getDescription());
        loc.setTotalRating(updatedLocation.getTotalRating());
        return locationRepository.save(loc);
    }

    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }
}
