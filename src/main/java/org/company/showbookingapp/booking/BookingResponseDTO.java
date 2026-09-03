package org.company.showbookingapp.booking;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class BookingResponseDTO {
    private Long bookingId;
    private String movieTitle;
    private LocalDate showDate;
    private LocalTime showStartTime;
    private List<String> seats;
    private BigDecimal totalAmount;
    private LocalDateTime bookingTime;
    private BookingStatus status;
}