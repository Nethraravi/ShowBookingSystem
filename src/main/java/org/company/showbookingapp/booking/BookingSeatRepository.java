package org.company.showbookingapp.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long> {
    @Query("""
        SELECT bs
        FROM BookingSeat bs
        WHERE bs.booking.id = :bookingId
        """)
    List<BookingSeat> findByBookingId(@Param("bookingId") Long bookingId);
}
