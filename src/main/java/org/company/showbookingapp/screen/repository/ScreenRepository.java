package org.company.showbookingapp.screen.repository;

import org.company.showbookingapp.screen.entity.Screen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScreenRepository extends JpaRepository<Screen, Long> {
    List<Screen> findByVenue_Id(Long venueId);

    boolean existsByNameAndVenue_Id(String name, Long venueId); //to check while creating if a screen with same name already exists

    boolean existsByNameAndVenue_IdAndIdNot(String name, Long venueId, Long screenId); //to check while updating
}
