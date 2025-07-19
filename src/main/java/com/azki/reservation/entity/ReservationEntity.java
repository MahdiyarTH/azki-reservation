package com.azki.reservation.entity;

import com.azki.availableslot.entity.AvailableSlotEntity;
import com.azki.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservations")
public class ReservationEntity {

    @Id
    @SequenceGenerator(name = "available_slot_id_seq", sequenceName = "reservations_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "available_slot_id_seq")
    private long id;

    @Column(name = "user_id", insertable = false, updatable = false)
    private long userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "slot_id", insertable = false, updatable = false)
    private long slotId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private AvailableSlotEntity slot;

    @Column(name = "reserved_date")
    private LocalDateTime reservedDate;

    @PrePersist
    public void prePersist() {
        reservedDate = LocalDateTime.now();
    }

}
