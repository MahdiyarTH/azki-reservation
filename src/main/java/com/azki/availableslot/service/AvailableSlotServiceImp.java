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
