package code.bookings;

import code.bookings.dto.BookingDto;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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

    @OneToOne
    private Seat seat;

    private String username;

    void cancel(Clock clock) {
        this.seat.cancelBooking(clock);
    }

    BookingDto toDto() {
        return new BookingDto(
                this.id,
                this.firstName,
                this.lastName,
                this.seat.toDto()
        );
    }
}
