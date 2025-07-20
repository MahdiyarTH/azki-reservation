package com.azki.availableslot.service;

import com.azki.availableslot.entity.AvailableSlotEntity;
import com.azki.availableslot.model.crud.CreateAvailableSlotRequest;

import java.util.Optional;

public interface AvailableSlotService {

    Optional<Long> reserveNextAvailableSlot();

    void createAvailableSlot(CreateAvailableSlotRequest createAvailableSlotRequest);

    AvailableSlotEntity get(long id);

}
