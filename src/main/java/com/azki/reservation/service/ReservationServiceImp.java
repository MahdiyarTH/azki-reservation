package com.azki.reservation.service;

import com.azki.availableslot.entity.AvailableSlotEntity;
import com.azki.availableslot.model.AvailableSlotMapper;
import com.azki.availableslot.service.AvailableSlotService;
import com.azki.common.exception.mode.ApiException;
import com.azki.reservation.entity.ReservationEntity;
import com.azki.reservation.model.crud.ReservationResponse;
import com.azki.reservation.repository.ReservationRepository;
import com.azki.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImp implements ReservationService {

    private final UserService userService;
    private final AvailableSlotMapper availableSlotMapper;
    private final AvailableSlotService availableSlotService;
    private final ReservationRepository reservationRepository;

    @Override
    @Transactional
    public ReservationResponse reserveBestSlot(long userId) {
        log.info("Trying to get best slot for user {}", userId);

        Long availableSlotId = availableSlotService.reserveNextAvailableSlot()
                .orElseThrow(
                        () -> {
                            log.warn("Could not get best slot for user {}", userId);

                            return ApiException
                                    .builder()
                                    .message("No available slot found")
                                    .httpStatus(HttpStatus.NOT_FOUND)
                                    .build();
                        }
                );

        AvailableSlotEntity availableSlot = availableSlotService.get(availableSlotId);

        log.info("Saving a reservation for user {}", userId);
        final ReservationEntity reservation = reservationRepository.save(
                ReservationEntity.builder()
                        .slot(availableSlot)
                        .user(userService.get(userId))
                        .build()
        );

        return ReservationResponse.builder()
                .id(reservation.getId())
                .slot(availableSlotMapper.toDto(availableSlot))
                .build();
    }

}
