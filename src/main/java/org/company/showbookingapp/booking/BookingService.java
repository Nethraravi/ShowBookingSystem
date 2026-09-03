package org.company.showbookingapp.booking;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.company.showbookingapp.exception.DuplicateResourceException;
import org.company.showbookingapp.exception.ResourceNotFoundException;
import org.company.showbookingapp.show.Show;
import org.company.showbookingapp.show.ShowRepository;
import org.company.showbookingapp.showSeat.ShowSeat;
import org.company.showbookingapp.showSeat.ShowSeatRepository;
import org.company.showbookingapp.showSeat.ShowSeatStatus;
import org.company.showbookingapp.user.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final ShowRepository showRepository;
    private final ShowSeatRepository showSeatRepository;

    @Transactional
    public BookingResponseDTO createBooking( BookingRequestDTO request, User user) {

        if (request.getSeatIds().size() != request.getSeatIds().stream().distinct().count()) {
            throw new DuplicateResourceException("Duplicate seat IDs are not allowed");
        }

        // Find Show
        Show show = showRepository.findById(request.getShowId())
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));

        // Find requested ShowSeats
        List<ShowSeat> showSeats = new ArrayList<>();

        for (Long showSeatId : request.getSeatIds()) {
            ShowSeat showSeat = showSeatRepository.findById(showSeatId)
                    .orElseThrow(() ->new ResourceNotFoundException("ShowSeat not found: " + showSeatId));

            if (!showSeat.getShow().getId().equals(show.getId())) {
                throw new DuplicateResourceException("Seat does not belong to the selected show");
            }

            if (showSeat.getStatus() != ShowSeatStatus.AVAILABLE) {
                throw new DuplicateResourceException("Seat is not available for booking");
            }

            showSeats.add(showSeat);
        }

        // Calculate total
        BigDecimal totalAmount = showSeats.stream().map(ShowSeat::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        // Create Booking
        Booking booking = new Booking();

        booking.setUser(user);
        booking.setShow(show);
        booking.setTotalAmount(totalAmount);
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus(BookingStatus.PENDING);

        Booking savedBooking = bookingRepository.save(booking);

        // Create and save BookingSeat records
        for (ShowSeat showSeat : showSeats) {

            BookingSeat bookingSeat = new BookingSeat();

            bookingSeat.setBooking(savedBooking);
            bookingSeat.setShowSeat(showSeat);

            bookingSeatRepository.save(bookingSeat);
            showSeat.setStatus(ShowSeatStatus.LOCKED);
            showSeatRepository.save(showSeat);
        }
        return mapToResponse(savedBooking, showSeats);
    }

    private BookingResponseDTO mapToResponse(Booking booking,List<ShowSeat> showSeats) {
        BookingResponseDTO response = new BookingResponseDTO();
        response.setBookingId(booking.getId());
        response.setMovieTitle(booking.getShow().getMovie().getTitle());
        response.setShowDate(booking.getShow().getStartTime().toLocalDate());

        response.setShowStartTime(booking.getShow().getStartTime().toLocalTime());

        response.setSeats(showSeats.stream().map(showSeat ->showSeat.getSeat().getRowLabel() + showSeat.getSeat().getSeatNumber()).toList());

        response.setTotalAmount(booking.getTotalAmount());
        response.setBookingTime(booking.getBookingTime());
        response.setStatus(booking.getStatus());

        return response;
    }

    public List<BookingResponseDTO> getMyBookings(User user) {

        List<Booking> bookings =bookingRepository.findByUserId(user.getId());

        return bookings.stream().map(booking -> {
                    List<ShowSeat> showSeats =bookingSeatRepository.findByBookingId(booking.getId()).stream().map(BookingSeat::getShowSeat).toList();
                    return mapToResponse(booking, showSeats);
        }).toList();
    }

    public BookingResponseDTO getBookingById(Long bookingId, User user) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->new ResourceNotFoundException("Booking not found"));

        if (!booking.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Booking not found");
        }

        List<ShowSeat> showSeats =bookingSeatRepository.findByBookingId(booking.getId()).stream().map(BookingSeat::getShowSeat).toList();

        return mapToResponse(booking, showSeats);
    }

    @Transactional
    public BookingResponseDTO cancelBooking(Long bookingId, User user) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->new ResourceNotFoundException("Booking not found"));

        if (!booking.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Booking not found");
        }

        List<BookingSeat> bookingSeats =bookingSeatRepository.findByBookingId(booking.getId());
        for (BookingSeat bookingSeat : bookingSeats) {

            ShowSeat showSeat = bookingSeat.getShowSeat();
            showSeat.setStatus(ShowSeatStatus.AVAILABLE);
            showSeatRepository.save(showSeat);
        }

        booking.setStatus(BookingStatus.CANCELLED);
        Booking updatedBooking = bookingRepository.save(booking);
        List<ShowSeat> showSeats =bookingSeatRepository.findByBookingId(booking.getId()).stream().map(BookingSeat::getShowSeat).toList();
        return mapToResponse(updatedBooking, showSeats);
    }
}