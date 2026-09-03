package org.company.showbookingapp.showSeat;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ShowSeatResponseDTO {

    private Long id;
    private Long showId;
    private Long seatId;
    private BigDecimal price;
    private ShowSeatStatus status;

}