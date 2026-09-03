package org.company.showbookingapp.screen.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.company.showbookingapp.screen.entity.Screen;
import org.company.showbookingapp.screen.service.ScreenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/venues/{venueId}/screens")
@RequiredArgsConstructor
public class ScreenController {

    private final ScreenService screenService;

    @PostMapping
    public Screen createScreen(@PathVariable Long venueId, @Valid @RequestBody Screen screen)
    {
        return screenService.createScreen(venueId, screen);
    }

    @GetMapping
    public List<Screen> getScreensByVenue(@PathVariable Long venueId)
    {
        return screenService.getScreenByVenue(venueId);
    }

    @PutMapping("/{screenId}")
    public Screen updateScreen(@PathVariable Long venueId, @PathVariable Long screenId, @Valid @RequestBody Screen screen)
    {
        return screenService.updateScreen(venueId, screenId, screen);
    }

    @DeleteMapping("/{screenId}")
    public ResponseEntity<String> deleteScreen(@PathVariable Long venueId, @PathVariable Long screenId)
    {
        screenService.deleteScreen(venueId, screenId);
        return ResponseEntity.ok("Screen deleted successfully");
    }
}
