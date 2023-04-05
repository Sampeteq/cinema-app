package code.bookings.domain;

import code.bookings.domain.exceptions.BookingException;
import code.bookings.application.dto.SeatDto;
import code.films.domain.Screening;
import lombok.*;

import javax.persistence.*;
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
            throw new BookingException("Seat not available");
        }
        if (screening.timeToScreeningStartInHours(clock) < 24) {
            throw new BookingException("Too late to booking");
        }
        this.status = SeatStatus.BUSY;
    }

    public void cancelBooking(Clock clock) {
        if (screening.timeToScreeningStartInHours(clock) < 24) {
            throw new BookingException("Too late to cancel booking");
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