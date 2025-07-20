package com.azki.reservation.entity;

import com.azki.availableslot.entity.AvailableSlotEntity;
import com.azki.common.data.persistableenum.AbstractPersistableEnumConverter;
import com.azki.common.data.persistableenum.PersistableEnum;
import com.azki.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservations")
@Cache(region = "reservation", usage = CacheConcurrencyStrategy.READ_WRITE)
public class ReservationEntity {

    @Id
    @SequenceGenerator(name = "available_slot_id_seq", sequenceName = "reservations_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "available_slot_id_seq")
    private long id;

    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "slot_id", insertable = false, updatable = false)
    //We use Long over long, because when using Examples, slotId evaluate to 0 when no value is set
    private Long slotId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private AvailableSlotEntity slot;

    @Column(name = "reserved_date")
    private LocalDateTime reservedDate;

    @Column(name = "canceled_date")
    private LocalDateTime canceledDate;

    @PrePersist
    public void prePersist() {
        reservedDate = LocalDateTime.now();
    }

    @Column(name = "status")
    @Convert(converter = Status.Converter.class)
    private Status status;

    @Getter
    @RequiredArgsConstructor
    public enum Status implements PersistableEnum<Integer> {
        CANCELLED(0),
        RESERVED(1);

        private final Integer dbValue;

        public static class Converter extends AbstractPersistableEnumConverter<Status, Integer> {
            public Converter() {
                super(Status.class);
            }
        }
    }
}
