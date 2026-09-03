package org.company.showbookingapp.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.company.showbookingapp.security.CustomUserDetails;
import org.company.showbookingapp.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(@Valid @RequestBody BookingRequestDTO request,Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        BookingResponseDTO response =bookingService.createBooking(request, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<BookingResponseDTO>> getMyBookings(Authentication authentication) {
        CustomUserDetails userDetails =(CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        List<BookingResponseDTO> response =bookingService.getMyBookings(user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingById(@PathVariable Long id,Authentication authentication) {
        CustomUserDetails userDetails =(CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        BookingResponseDTO response =bookingService.getBookingById(id, user);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<BookingResponseDTO> cancelBooking(@PathVariable Long id,Authentication authentication) {
        CustomUserDetails userDetails =(CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        BookingResponseDTO response =bookingService.cancelBooking(id, user);
        return ResponseEntity.ok(response);
    }
}