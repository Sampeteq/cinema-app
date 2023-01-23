package code.bookings;

import code.bookings.dto.BookingDto;
import code.bookings.exception.BookingException;
import lombok.*;

import javax.persistence.*;
import java.time.Clock;
import java.util.UUID;

@Entity
@Table(name = "BOOKINGS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(of = "id")
@ToString
class Booking {

    @Id
    private UUID id;

    private String firstName;

    private String lastName;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @OneToOne
    private Seat seat;

    private String username;

    void cancel(Clock clock) {
        if (this.status.equals(BookingStatus.CANCELLED)) {
            throw new BookingException("Booking already cancelled");
        }
        this.seat.cancelBooking(clock);
        this.status = BookingStatus.CANCELLED;
    }

    BookingDto toDto() {
        return new BookingDto(
                this.id,
                this.firstName,
                this.lastName,
                this.status.name(),
                this.seat.toDto()
        );
    }
}
