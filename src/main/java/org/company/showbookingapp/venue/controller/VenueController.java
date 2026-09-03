package org.company.showbookingapp.venue.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.company.showbookingapp.venue.entity.Venue;
import org.company.showbookingapp.venue.service.VenueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/venues")
@RequiredArgsConstructor
public class VenueController {

    private final VenueService venueService;

    @PostMapping
    public Venue createVenue(@Valid @RequestBody Venue venue)
    {
        return venueService.createVenue(venue);
    }

    @GetMapping
    public List<Venue> getAllVenues()
    {
        return venueService.getAllVenues();
    }

    @PutMapping("/{id}")
    public Venue updateVenue(@PathVariable Long id, @Valid @RequestBody Venue venue)
    {
        return venueService.updateVenue(id, venue);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVenue(@PathVariable Long id)
    {
        venueService.deleteVenue(id);
        return ResponseEntity.ok("Venue deleted successfully");
    }
}
