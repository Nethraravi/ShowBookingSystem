package org.company.showbookingapp.showSeat;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.company.showbookingapp.security.CustomUserDetails;
import org.company.showbookingapp.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/show-seats")
@RequiredArgsConstructor
public class ShowSeatController {

    private final ShowSeatService showSeatService;

    @PostMapping
    public ResponseEntity<ShowSeatResponseDTO> createShowSeat(@Valid @RequestBody ShowSeatRequestDTO request) {

        ShowSeatResponseDTO response = showSeatService.createShowSeat(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ShowSeatResponseDTO>> getAllShowSeats() {

        return ResponseEntity.ok(showSeatService.getAllShowSeats());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowSeatResponseDTO> getShowSeatById(
            @PathVariable Long id) {

        return ResponseEntity.ok(showSeatService.getShowSeatById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShowSeatResponseDTO> updateShowSeat(
            @PathVariable Long id,
            @Valid @RequestBody ShowSeatRequestDTO request) {

        return ResponseEntity.ok(showSeatService.updateShowSeat(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShowSeat(@PathVariable Long id) {

        showSeatService.deleteShowSeat(id);
        return ResponseEntity.noContent().build();
    }

}