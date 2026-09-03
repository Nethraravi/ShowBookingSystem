package org.company.showbookingapp.screen.service;

import lombok.RequiredArgsConstructor;
import org.company.showbookingapp.exception.DuplicateResourceException;
import org.company.showbookingapp.exception.ResourceNotFoundException;
import org.company.showbookingapp.screen.entity.Screen;
import org.company.showbookingapp.screen.repository.ScreenRepository;
import org.company.showbookingapp.venue.entity.Venue;
import org.company.showbookingapp.venue.repository.VenueRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScreenService {

    private final ScreenRepository screenRepository;
    private final VenueRepository venueRepository;

    public Screen createScreen(Long venueId, Screen screen)
    {
        Venue venue = venueRepository.findById(venueId).orElseThrow(() -> new ResourceNotFoundException("Venue not found"));
        boolean screenExists = screenRepository.existsByNameAndVenue_Id(screen.getName(), venueId);
        if(screenExists)
        {
            throw new DuplicateResourceException("Screen already exists in this venue");
        }
        screen.setVenue(venue);
        screen.setIntervalDuration(screen.getIntervalDuration());
        screen.setBufferDuration(screen.getBufferDuration());
        return screenRepository.save(screen);
    }

    public List<Screen> getScreenByVenue(Long venueId)
    {
        venueRepository.findById(venueId).orElseThrow(() -> new ResourceNotFoundException("Venue not found"));
        return screenRepository.findByVenue_Id(venueId);
    }

    public Screen updateScreen(Long venueId, Long screenId, Screen updatedScreen)
    {
        Venue venue = venueRepository.findById(venueId).orElseThrow(() -> new ResourceNotFoundException("Venue not found"));
        Screen existingScreen = screenRepository.findById(screenId).orElseThrow(() -> new ResourceNotFoundException("Screen not found"));

        if(!existingScreen.getVenue().getId().equals(venueId))
        {
            throw new ResourceNotFoundException("Screen does not belong to this venue");
        }

        boolean screenExists = screenRepository.existsByNameAndVenue_IdAndIdNot(updatedScreen.getName(),venueId,screenId);

        if(screenExists)
        {
            throw new DuplicateResourceException("Screen already exists in this venue");
        }

        existingScreen.setName(updatedScreen.getName());
        existingScreen.setIntervalDuration(updatedScreen.getIntervalDuration());
        existingScreen.setBufferDuration(updatedScreen.getBufferDuration());
        return screenRepository.save(existingScreen);
    }

    public void deleteScreen(Long venueId, Long screenId)
    {
        venueRepository.findById(venueId).orElseThrow(() -> new ResourceNotFoundException("Venue not found"));
        Screen existingScreen = screenRepository.findById(screenId).orElseThrow(() -> new ResourceNotFoundException("Screen not found"));

        if(!existingScreen.getVenue().getId().equals(venueId))
        {
            throw new ResourceNotFoundException("Screen does not belong to this venue");
        }
        screenRepository.delete(existingScreen);
    }
}
