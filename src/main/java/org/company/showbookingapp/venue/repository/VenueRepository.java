package org.company.showbookingapp.venue.repository;

import org.company.showbookingapp.venue.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueRepository extends JpaRepository<Venue, Long> {

    boolean existsByNameAndAddressAndCity(String name, String address, String city);

    boolean existsByNameAndAddressAndCityAndIdNot(String name, String address, String city, Long id);
}
