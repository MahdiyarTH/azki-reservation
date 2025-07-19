package com.azki.availableslot.service;

import com.azki.availableslot.entity.AvailableSlotEntity;
import com.azki.availableslot.model.crud.CreateAvailableSlotRequest;

public interface AvailableSlotService {

    void createAvailableSlot(CreateAvailableSlotRequest createAvailableSlotRequest);

    AvailableSlotEntity get(long id);

}
