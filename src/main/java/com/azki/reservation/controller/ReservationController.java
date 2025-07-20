package com.azki.reservation.controller;

import com.azki.common.model.HttpResponse;
import com.azki.reservation.model.crud.ReservationResponse;
import com.azki.reservation.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Reservation", description = "Reservation related APIs")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    @PreAuthorize("isFullyAuthenticated()")
    @Operation(
            summary = "Reserve an open slot",
            description = "Reserve the closest available slot for current user"
    )
    public HttpResponse<ReservationResponse> reserve(@AuthenticationPrincipal Long userId) {
        return HttpResponse.success(reservationService.reserveBestSlot(userId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isFullyAuthenticated()")
    @Operation(
            summary = "Cancel reservation",
            description = "Cancel the reservation specified by passed id"
    )
    public HttpResponse<Void> deleteReservation(
            @AuthenticationPrincipal Long userId,
            @Parameter(required = true, description = "Reservation id that got from POST /reservations API") @PathVariable("id") Long id) {
        reservationService.deleteReservation(id, userId);
        return HttpResponse.success();
    }

}
