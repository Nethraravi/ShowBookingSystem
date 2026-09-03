package org.company.showbookingapp.seat;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SeatRequestDTO {

    @NotNull(message = "Screen ID is required")
    private Long screenId;

    @NotBlank(message = "Row label is required")
    private String rowLabel;

    @NotNull(message = "Seat number is required")
    @Min(value = 1, message = "Seat number must be at least 1")
    private Integer seatNumber;

    @NotNull(message = "Seat type is required")
    private SeatType seatType;
}