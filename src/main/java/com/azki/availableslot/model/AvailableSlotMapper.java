package com.azki.availableslot.model;

import com.azki.availableslot.entity.AvailableSlotEntity;
import com.azki.availableslot.model.dto.AvailableSlotDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AvailableSlotMapper {

    AvailableSlotDto toDto(AvailableSlotEntity entity);

}
