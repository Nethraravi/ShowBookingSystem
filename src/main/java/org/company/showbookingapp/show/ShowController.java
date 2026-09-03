package org.company.showbookingapp.show;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shows")
@RequiredArgsConstructor
public class ShowController {
    private final ShowService showService;

    @PostMapping
    public ResponseEntity<ShowResponseDTO> createShow( @Valid @RequestBody CreateShowRequestDTO request) {

        ShowResponseDTO show = showService.createShow(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(show);
    }

    @GetMapping
    public ResponseEntity<List<ShowResponseDTO>> getAllShows() {
        return ResponseEntity.ok(showService.getAllShows());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowResponseDTO> getShowById(@PathVariable Long id) {

        return ResponseEntity.ok(showService.getShowById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShow(@PathVariable Long id) {

        showService.deleteShow(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShowResponseDTO> updateShow(
            @PathVariable Long id,
            @Valid @RequestBody UpdateShowRequestDTO request) {

        return ResponseEntity.ok(
                showService.updateShow(id, request)
        );
    }
}
