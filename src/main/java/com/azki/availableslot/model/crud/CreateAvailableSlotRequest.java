package com.azki.availableslot.model.crud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAvailableSlotRequest {

    private LocalDateTime endTime;

    private LocalDateTime startTime;

}
