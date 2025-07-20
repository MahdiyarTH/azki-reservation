package com.azki.availableslot.repository;

import com.azki.availableslot.entity.AvailableSlotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailableSlotRepository extends JpaRepository<AvailableSlotEntity, Long> {

    @Modifying
    @Query("update AvailableSlotEntity available set available.reserved = false where available.id = :id")
    void undoReservation(@Param("id") long id);

}
