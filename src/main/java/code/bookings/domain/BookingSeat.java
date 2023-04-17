package code.bookings.domain;

import code.bookings.domain.exceptions.SeatNotAvailableException;
import code.bookings.domain.exceptions.TooLateToBookingException;
import code.bookings.domain.exceptions.TooLateToCancelBookingException;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "BOOKINGS_SEATS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@EqualsAndHashCode(of = "id")
@ToString
public class BookingSeat {

    @Id
    private UUID id;

    private boolean isAvailable;

    private int timeToScreeningInHours;

    public void book() {
        if (!this.isAvailable) {
            throw new SeatNotAvailableException();
        }
        if (timeToScreeningInHours < 24) {
            throw new TooLateToBookingException();
        }
        this.isAvailable = false;
    }

    public void cancelBooking() {
        if (timeToScreeningInHours < 24) {
            throw new TooLateToCancelBookingException();
        }
    }
}
