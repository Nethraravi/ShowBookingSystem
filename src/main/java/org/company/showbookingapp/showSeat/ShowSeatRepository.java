package org.company.showbookingapp.showSeat;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {
    boolean existsByShowIdAndSeatId(Long showId, Long seatId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ss FROM ShowSeat ss WHERE ss.id = :id")
    Optional<ShowSeat> findByIdForUpdate(@Param("id") Long id);
}
