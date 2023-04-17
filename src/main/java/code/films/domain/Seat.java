package code.films.domain;

import code.bookings.domain.events.DecreasedFreeSeatsEvent;
import code.bookings.domain.events.IncreasedFreeSeatsEvent;
import code.bookings.domain.exceptions.SeatNotAvailableException;
import code.bookings.domain.exceptions.TooLateToBookingException;
import code.bookings.domain.exceptions.TooLateToCancelBookingException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Clock;
import java.util.UUID;

@Entity
@Table(name = "SEATS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Getter
@ToString
public class Seat {

    @Id
    @Getter
    private UUID id;

    private int rowNumber;

    private int number;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @ManyToOne
    private Screening screening;

    public boolean isFree() {
        return status.equals(SeatStatus.FREE);
    }

    public void handleEvent(DecreasedFreeSeatsEvent event) {
        this.status = SeatStatus.BUSY;
    }

    public void handleEvent(IncreasedFreeSeatsEvent event) {
        this.status = SeatStatus.FREE;
    }
}