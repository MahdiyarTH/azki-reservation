package com.azki.reservation.model.crud;

import com.azki.availableslot.model.dto.AvailableSlotDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponse {

    private long id;

    private AvailableSlotDto slot;

}
