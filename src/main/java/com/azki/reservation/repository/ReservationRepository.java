package com.azki.reservation.repository;

import com.azki.reservation.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    @Modifying
    @Query("update ReservationEntity r set r.status = 0, r.canceledDate = :cancelDate where r.id = :id")
    void cancelReservation(@Param("cancelDate") LocalDateTime cancelDate, @Param("id") long id);

}
