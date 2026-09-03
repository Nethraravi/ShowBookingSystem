package org.company.showbookingapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.company.showbookingapp.seat.SeatRequestDTO;
import org.company.showbookingapp.seat.SeatResponseDTO;
import org.company.showbookingapp.seat.SeatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @PostMapping
    public ResponseEntity<SeatResponseDTO> createSeat(@Valid @RequestBody SeatRequestDTO request) {
        SeatResponseDTO response = seatService.createSeat(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<SeatResponseDTO>> getAllSeats() {
        return ResponseEntity.ok(seatService.getAllSeats());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeatResponseDTO> getSeatById(@PathVariable Long id) {
        return ResponseEntity.ok(seatService.getSeatById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeatResponseDTO> updateSeat(@PathVariable Long id,@Valid @RequestBody SeatRequestDTO request) {
        return ResponseEntity.ok(seatService.updateSeat(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeat(@PathVariable Long id) {
        seatService.deleteSeat(id);
        return ResponseEntity.noContent().build();
    }
}