package com.orbidroid.orbidroid_backend.repository;

import com.orbidroid.orbidroid_backend.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, String> {
    public static final String GET_BOOKINGS_IN_BETWEEN =
            "SELECT * FROM bookings where booking_time>=:datetimeStart and booking_time<=:datetimeEnd";
    List<Booking> getByDocNum(Integer docNum);

    List<Booking> getByStuNum(Integer stuNum);

    String deleteByStuNum(Integer stuNum);

    String removeById(Integer id);

    Optional<Booking> getById(Integer id);

    @Query(value = GET_BOOKINGS_IN_BETWEEN, nativeQuery = true)
    List<Booking> getBookingsInBetween(
            @Param("datetimeStart") String datetimeStart,
            @Param("datetimeEnd") String datetimeEnd
    );
}
