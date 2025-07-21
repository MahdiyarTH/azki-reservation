package com.azki.availableslot.service;

import com.azki.availableslot.entity.AvailableSlotEntity;
import com.azki.availableslot.model.crud.CreateAvailableSlotRequest;
import com.azki.availableslot.repository.AvailableSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AvailableSlotServiceImp implements AvailableSlotService {

    private final JdbcTemplate jdbcTemplate;
    private final AvailableSlotRepository repository;

    @Override
    @Transactional
    public Optional<Long> reserveNextAvailableSlot() {
        /*
        Here we used select for update, to prevent locking rows for other transactions.
        In this query, when a row is selected, database put a lock on the row, when a new
        transaction execute same query, it will NOT get same row as first transaction, because
        it is marked as LOCKED, also it will not wait for the first transaction to release the
        lock, so database return the next first not marked as lock row. In this way execution
        time reduce dramatically.
        Also by putting this query in repository layer, we will not be able to get the selected row
        data as return, because @Modifying methods must only return void or Integer/int that indicates
        the number of affected rows.
         */
        String sql = """
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
                    RETURNING id;
                """;

        List<Long> result = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("id"));

        return result.stream().findFirst();
    }

    @Override
    @Transactional
    public void undoReservation(long availableSlotId) {
        repository.undoReservation(availableSlotId);
    }

    @Override
    @Transactional
    public void createAvailableSlot(CreateAvailableSlotRequest createAvailableSlotRequest) {
        repository.save(
                AvailableSlotEntity.builder()
                        .startTime(createAvailableSlotRequest.getStartTime())
                        .endTime(createAvailableSlotRequest.getEndTime())
                        .reserved(false)
                        .build()
        );
    }

    @Override
    public AvailableSlotEntity get(long id) {
        return repository.getReferenceById(id);
    }

}
