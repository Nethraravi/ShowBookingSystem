package org.company.showbookingapp.seat;

import org.company.showbookingapp.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    boolean existsByScreenIdAndRowLabelAndSeatNumber(Long screenId,String rowLabel,Integer seatNumber);
}
