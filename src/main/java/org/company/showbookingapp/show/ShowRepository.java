package org.company.showbookingapp.show;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ShowRepository extends JpaRepository<Show, Long> {

    @Query("""
    SELECT COUNT(s) > 0 FROM Show s WHERE s.screen.id = :screenId AND s.startTime < :newOccupiedUntil AND s.screenAvailableTime > :newStartTime""")
    boolean existsOverlappingShow(@Param("screenId") Long screenId,
                                  @Param("newStartTime")LocalDateTime newStartTime,
                                  @Param("newOccupiedUntil") LocalDateTime newOccupiedUntil);

    @Query("""
    SELECT COUNT(s) > 0
    FROM Show s
    WHERE s.screen.id = :screenId
      AND s.id <> :showId
      AND s.startTime < :screenAvailableTime
      AND s.screenAvailableTime > :startTime""")
    boolean existsOverlappingShowExcludingId(
            @Param("screenId") Long screenId,
            @Param("startTime") LocalDateTime startTime,
            @Param("screenAvailableTime") LocalDateTime screenAvailableTime,
            @Param("showId") Long showId
    );
}
