package org.company.showbookingapp.seat;

import lombok.RequiredArgsConstructor;
import org.company.showbookingapp.Seat;
import org.company.showbookingapp.exception.DuplicateResourceException;
import org.company.showbookingapp.exception.ResourceNotFoundException;
import org.company.showbookingapp.screen.entity.Screen;
import org.company.showbookingapp.screen.repository.ScreenRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final ScreenRepository screenRepository;

    public SeatResponseDTO createSeat(SeatRequestDTO request) {

        Screen screen = screenRepository.findById(request.getScreenId())
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found"));

        if (seatRepository.existsByScreenIdAndRowLabelAndSeatNumber(request.getScreenId(),request.getRowLabel(),request.getSeatNumber())) {

            throw new DuplicateResourceException("Seat already exists");
        }

        Seat seat = new Seat();
        seat.setScreen(screen);
        seat.setRowLabel(request.getRowLabel());
        seat.setSeatNumber(request.getSeatNumber());
        seat.setSeatType(request.getSeatType());

        Seat savedSeat = seatRepository.save(seat);

        SeatResponseDTO response = new SeatResponseDTO();
        response.setId(savedSeat.getId());
        response.setScreenId(savedSeat.getScreen().getId());
        response.setRowLabel(savedSeat.getRowLabel());
        response.setSeatNumber(savedSeat.getSeatNumber());
        response.setSeatType(savedSeat.getSeatType());

        return response;
    }

    public List<SeatResponseDTO> getAllSeats() {

        return seatRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public SeatResponseDTO getSeatById(Long id) {

        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found"));

        return mapToResponse(seat);
    }

    public SeatResponseDTO updateSeat(Long id, SeatRequestDTO request) {

        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found"));

        Screen screen = screenRepository.findById(request.getScreenId())
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found"));

        seat.setScreen(screen);
        seat.setRowLabel(request.getRowLabel());
        seat.setSeatNumber(request.getSeatNumber());
        seat.setSeatType(request.getSeatType());

        Seat updatedSeat = seatRepository.save(seat);

        return mapToResponse(updatedSeat);
    }

    public void deleteSeat(Long id) {

        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found"));

        seatRepository.delete(seat);
    }

    private SeatResponseDTO mapToResponse(Seat seat) {

        SeatResponseDTO response = new SeatResponseDTO();

        response.setId(seat.getId());
        response.setScreenId(seat.getScreen().getId());
        response.setRowLabel(seat.getRowLabel());
        response.setSeatNumber(seat.getSeatNumber());
        response.setSeatType(seat.getSeatType());

        return response;
    }
}