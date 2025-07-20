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
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

        log.info("Saving a reservation for user {} by slot id {}", userId, availableSlotId);
        final ReservationEntity reservation = reservationRepository.save(
                ReservationEntity.builder()
                        .slot(availableSlot)
                        .user(userService.get(userId))
                        .status(ReservationEntity.Status.RESERVED)
                        .build()
        );

        return ReservationResponse.builder()
                .id(reservation.getId())
                .slot(availableSlotMapper.toDto(availableSlot))
                .build();
    }

    @Override
    @Transactional
    public void deleteReservation(long reservationId, long userId) {
        ReservationEntity reservation = reservationRepository
                .findOne(
                        Example.of(
                                ReservationEntity.builder()
                                        .userId(userId)
                                        .id(reservationId)
                                        .build()
                        )
                )
                .orElseThrow(
                        () -> ApiException
                                .builder()
                                .httpStatus(HttpStatus.NOT_FOUND)
                                .message("No reservation found!")
                                .build()
                );

        if (reservation.getStatus().equals(ReservationEntity.Status.CANCELLED))
            throw ApiException.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("Reservation already canceled!")
                    .build();

        availableSlotService.undoReservation(reservation.getSlotId());
        reservationRepository.cancelReservation(LocalDateTime.now(), reservationId);
    }

}
