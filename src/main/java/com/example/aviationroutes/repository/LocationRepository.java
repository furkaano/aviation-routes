package com.example.aviationroutes.repository;

import com.example.aviationroutes.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByNameAndCountryAndCityAndLocationCode(String name, String country, String city, String locationCode);
}
