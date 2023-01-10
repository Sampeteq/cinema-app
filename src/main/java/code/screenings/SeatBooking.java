package code.screenings;

import code.screenings.dto.SeatBookingView;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.Clock;
import java.util.UUID;

@Entity
@Table(name = "SEATS_BOOKINGS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(of = "id")
@ToString
class SeatBooking {

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

    SeatBookingView toView() {
        return new SeatBookingView(
                this.id,
                this.firstName,
                this.lastName,
                this.seat.toView()
        );
    }
}
