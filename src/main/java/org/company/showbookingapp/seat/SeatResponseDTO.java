package org.company.showbookingapp.seat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeatResponseDTO {

    private Long id;
    private Long screenId;
    private String rowLabel;
    private Integer seatNumber;
    private SeatType seatType;
}