package com.azki.availableslot.service;

import com.azki.availableslot.entity.AvailableSlotEntity;
import com.azki.availableslot.model.crud.CreateAvailableSlotRequest;
import com.azki.availableslot.repository.AvailableSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AvailableSlotServiceImp implements AvailableSlotService {

    private final AvailableSlotRepository repository;

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
