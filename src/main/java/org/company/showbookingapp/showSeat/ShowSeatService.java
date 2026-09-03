package org.company.showbookingapp.showSeat;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.company.showbookingapp.Seat;
import org.company.showbookingapp.exception.DuplicateResourceException;
import org.company.showbookingapp.exception.ResourceNotFoundException;
import org.company.showbookingapp.seat.SeatRepository;
import org.company.showbookingapp.show.Show;
import org.company.showbookingapp.show.ShowRepository;
import org.company.showbookingapp.user.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowSeatService {

    private final ShowSeatRepository showSeatRepository;
    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;

    public ShowSeatResponseDTO createShowSeat(ShowSeatRequestDTO request) {

        Show show = showRepository.findById(request.getShowId())
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));

        Seat seat = seatRepository.findById(request.getSeatId())
                .orElseThrow(() ->new ResourceNotFoundException("Seat not found"));

        if (showSeatRepository.existsByShowIdAndSeatId(request.getShowId(),request.getSeatId())) {

            throw new DuplicateResourceException("Seat is already assigned to this show");
        }

        ShowSeat showSeat = new ShowSeat();

        showSeat.setShow(show);
        showSeat.setSeat(seat);
        showSeat.setPrice(request.getPrice());
        showSeat.setStatus(ShowSeatStatus.AVAILABLE);

        ShowSeat savedShowSeat = showSeatRepository.save(showSeat);

        return mapToResponse(savedShowSeat);
    }

    public List<ShowSeatResponseDTO> getAllShowSeats() {

        return showSeatRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ShowSeatResponseDTO getShowSeatById(Long id) {

        ShowSeat showSeat = showSeatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ShowSeat not found"));

        return mapToResponse(showSeat);
    }

    public ShowSeatResponseDTO updateShowSeat(Long id, ShowSeatRequestDTO request) {

        ShowSeat showSeat = showSeatRepository.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException("ShowSeat not found"));

        Show show = showRepository.findById(request.getShowId())
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));

        Seat seat = seatRepository.findById(request.getSeatId())
                .orElseThrow(() ->new ResourceNotFoundException("Seat not found"));

        showSeat.setShow(show);
        showSeat.setSeat(seat);
        showSeat.setPrice(request.getPrice());

        ShowSeat updatedShowSeat = showSeatRepository.save(showSeat);

        return mapToResponse(updatedShowSeat);
    }

    public void deleteShowSeat(Long id) {

        ShowSeat showSeat = showSeatRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("ShowSeat not found"));

        showSeatRepository.delete(showSeat);
    }

    private ShowSeatResponseDTO mapToResponse(ShowSeat showSeat) {

        ShowSeatResponseDTO response = new ShowSeatResponseDTO();

        response.setId(showSeat.getId());
        response.setShowId(showSeat.getShow().getId());
        response.setSeatId(showSeat.getSeat().getId());
        response.setPrice(showSeat.getPrice());
        response.setStatus(showSeat.getStatus());

        return response;
    }

}