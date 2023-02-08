package code.bookings.domain;

import code.bookings.application.dto.BookingDto;
import code.bookings.domain.exceptions.BookingException;
import code.films.application.dto.SeatDetails;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "BOOKINGS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Getter
@ToString
public class Booking {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private UUID seatId;

    private String username;

    public static Booking make(UUID seatId, SeatDetails seatDetails, String username) {
        if (seatDetails.timeToScreeningInHours() < 24) {
            throw new BookingException("Too late to booking");
        }
        if (!seatDetails.isSeatAvailable()) {
            throw new BookingException("Seat not available");
        }
        return new Booking(
                UUID.randomUUID(),
                BookingStatus.ACTIVE,
                seatId,
                username
        );
    }

    public void cancel(SeatDetails seatDetails) {
        if (this.status.equals(BookingStatus.CANCELLED)) {
            throw new BookingException("Booking already cancelled");
        }
        if (seatDetails.timeToScreeningInHours() < 24) {
            throw new BookingException("Too late to cancel booking");
        }
        this.status = BookingStatus.CANCELLED;
    }

    public BookingDto toDto() {
        return new BookingDto(
                this.id,
                this.status.name(),
                this.seatId
        );
    }
}
