package com.azki.reservation.service;

import com.azki.reservation.model.crud.ReservationResponse;

public interface ReservationService {

    ReservationResponse reserveBestSlot(long userId);

    void deleteReservation(long reservationId, long userId);

}
