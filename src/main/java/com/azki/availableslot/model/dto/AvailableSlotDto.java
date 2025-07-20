package com.azki.availableslot.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AvailableSlotDto {

    private LocalDateTime endTime;

    private LocalDateTime startTime;

}
