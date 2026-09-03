package org.company.showbookingapp.venue.service;

import lombok.RequiredArgsConstructor;
import org.company.showbookingapp.exception.DuplicateResourceException;
import org.company.showbookingapp.exception.ResourceNotFoundException;
import org.company.showbookingapp.venue.entity.Venue;
import org.company.showbookingapp.venue.repository.VenueRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VenueService {

    private final VenueRepository venueRepository;

    public Venue createVenue(Venue venue)
    {
        boolean venueExists = venueRepository.existsByNameAndAddressAndCity(venue.getName(), venue.getAddress(), venue.getCity());

        if(venueExists)
        {
            throw new DuplicateResourceException("Venue already exists");
        }

        return venueRepository.save(venue);
    }

    public List<Venue> getAllVenues()
    {
        return venueRepository.findAll();
    }

    public Venue updateVenue(Long id, Venue updatedVenue)
    {
        Venue existingVenue = venueRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Venue not found"));

        boolean venueExists = venueRepository.existsByNameAndAddressAndCityAndIdNot(updatedVenue.getName(), updatedVenue.getAddress(), updatedVenue.getCity(), id);

        if(venueExists)
        {
            throw new DuplicateResourceException("Venue already exists");
        }

        existingVenue.setName(updatedVenue.getName());
        existingVenue.setAddress(updatedVenue.getAddress());
        existingVenue.setCity(updatedVenue.getCity());
        existingVenue.setStatus(updatedVenue.getStatus());

        return venueRepository.save(existingVenue);
    }

    public void deleteVenue(Long id)
    {
        Venue venue = venueRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Venue not found"));
        venueRepository.delete(venue);
    }

}
