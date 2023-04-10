package code.bookings.domain;

import code.bookings.domain.exceptions.SeatNotAvailableException;
import code.bookings.domain.exceptions.TooLateToBookingException;
import code.bookings.domain.exceptions.TooLateToCancelBookingException;
import code.bookings.infrastructure.rest.dto.SeatDto;
import code.films.domain.Screening;
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

    public void book(Clock clock) {
        if (status.equals(SeatStatus.BUSY)) {
            throw new SeatNotAvailableException();
        }
        if (screening.timeToScreeningStartInHours(clock) < 24) {
            throw new TooLateToBookingException();
        }
        this.status = SeatStatus.BUSY;
    }

    public void cancelBooking(Clock clock) {
        if (screening.timeToScreeningStartInHours(clock) < 24) {
            throw new TooLateToCancelBookingException();
        }
        this.status = SeatStatus.FREE;
    }

    public SeatDto toDto() {
        return new SeatDto(
                id,
                rowNumber,
                number,
                status.name()
        );
    }
}