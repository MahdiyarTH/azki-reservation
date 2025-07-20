package com.azki.reservation.controller;

import com.azki.common.model.HttpResponse;
import com.azki.reservation.model.crud.ReservationResponse;
import com.azki.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    @PreAuthorize("isFullyAuthenticated()")
    public HttpResponse<ReservationResponse> reserve(@AuthenticationPrincipal Long userId) {
        return HttpResponse.success(reservationService.reserveBestSlot(userId));
    }

}
