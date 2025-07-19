package com.azki.reservation.service;

import com.azki.availableslot.service.AvailableSlotService;
import com.azki.reservation.entity.ReservationEntity;
import com.azki.reservation.repository.ReservationRepository;
import com.azki.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationServiceImp implements ReservationService {

    private final UserService userService;
    private final AvailableSlotService availableSlotService;
    private final ReservationRepository reservationRepository;

    @Override
    @Transactional
    public void save(long userId, long slotId) {
        reservationRepository.save(
                ReservationEntity.builder()
                        .slot(availableSlotService.get(userId))
                        .user(userService.get(userId))
                        .build()
        );
    }
}
