package code.screenings;

import code.screenings.dto.ScreeningSeatDto;
import code.screenings.exception.BookingAlreadyCancelledException;
import code.screenings.exception.ScreeningSeatBusyException;
import code.screenings.exception.TooLateToBookingException;
import code.screenings.exception.TooLateToCancelBookingException;
import lombok.*;

import javax.persistence.*;
import java.time.Clock;
import java.util.UUID;

@Entity
@Table(name = "SCREENINGS_ROOMS_SEATS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(of = "id")
@ToString
class ScreeningRoomSeat {

    @Id
    @Getter
    private UUID id;

    private int rowNumber;

    private int number;

    @Enumerated(EnumType.STRING)
    private ScreeningSeatStatus status;

    @ManyToOne
    private Screening screening;

    void assignScreening(Screening screening) {
        this.screening = screening;
    }

    boolean isFree() {
        return this.status.equals(ScreeningSeatStatus.FREE);
    }

    void book(Clock clock) {
        if (this.screening.timeToScreeningStartInHours(clock) < 24) {
            throw new TooLateToBookingException();
        }
        if (this.status.equals(ScreeningSeatStatus.BUSY)) {
            throw new ScreeningSeatBusyException(this.screening.getId());
        }
        this.status = ScreeningSeatStatus.BUSY;
    }

    void cancelBooking(Clock clock) {
        if (this.status.equals(ScreeningSeatStatus.FREE)) {
            throw new BookingAlreadyCancelledException(this.id);
        }
        if (this.screening.timeToScreeningStartInHours(clock) < 24) {
            throw new TooLateToCancelBookingException(this.id);
        }
        this.status = ScreeningSeatStatus.FREE;
    }

    ScreeningSeatDto toDTO() {
        return new ScreeningSeatDto(
                this.id,
                this.rowNumber,
                this.number,
                this.status.name(),
                this.screening.getId()
        );
    }
}