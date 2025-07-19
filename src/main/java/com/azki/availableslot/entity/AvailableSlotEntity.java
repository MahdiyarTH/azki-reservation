package com.azki.availableslot.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@Table(
        name = "available_slots",
        //for better performance for finding available slot
        indexes = {@Index(name = "as_start_time_idx", columnList = "start_time")}
)
@NoArgsConstructor
@AllArgsConstructor
public class AvailableSlotEntity {

    @Id
    @SequenceGenerator(name = "available_slot_id_seq", sequenceName = "available_slots_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "available_slot_id_seq")
    private long id;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "is_reserved", columnDefinition = "BOOLEAN default false")
    private boolean reserved;

}
