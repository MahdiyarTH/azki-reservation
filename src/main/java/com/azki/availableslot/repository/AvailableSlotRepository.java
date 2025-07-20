package com.azki.availableslot.repository;

import com.azki.availableslot.entity.AvailableSlotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AvailableSlotRepository extends JpaRepository<AvailableSlotEntity, Long> {

    @Modifying
    @Query(value = """
            WITH slot AS (
                SELECT id
                FROM available_slots
                WHERE is_reserved = FALSE
                AND start_time >= now()
                ORDER BY start_time
                LIMIT 1
                FOR UPDATE SKIP LOCKED
            )
            UPDATE available_slots
            SET is_reserved = TRUE
            WHERE id IN (SELECT id FROM slot)
            RETURNING slot.id;
            """, nativeQuery = true)
    Optional<Long> reserveNextAvailableSlot();

}
