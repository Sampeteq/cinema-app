package code.bookings.domain;

import code.bookings.domain.exceptions.BookingException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "BOOKINGS_SEATS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode(of = "id")
@Getter
@ToString
public class BookingSeat {

    @Id
    private UUID id;

    private boolean isAvailable;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "screeningId")
    private BookingScreening screening;

    public void book() {
        if (screening.getTimeToScreeningInHours() < 24) {
            throw new BookingException("Too late to booking");
        }
        if (!isAvailable) {
            throw new BookingException("Seat not available");
        }
        isAvailable = false;
    }

    public void cancelBooking() {
        if (screening.getTimeToScreeningInHours() < 24) {
            throw new BookingException("Too late to cancel booking");
        }
        isAvailable = true;
    }
}
