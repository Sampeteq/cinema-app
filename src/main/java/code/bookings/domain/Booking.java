package code.bookings.domain;

import code.bookings.infrastructure.rest.dto.BookingDto;
import code.bookings.domain.exceptions.BookingException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.Clock;
import java.util.UUID;

@Entity
@Table(name = "BOOKINGS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@With
@Getter
@ToString
public class Booking {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "seatId")
    private Seat seat;

    private String username;

    public static Booking make(Seat seat, String username, Clock clock) {
        seat.book(clock);
        return new Booking(
                UUID.randomUUID(),
                BookingStatus.ACTIVE,
                seat,
                username
        );
    }

    public void cancel(Clock clock) {
        if (status.equals(BookingStatus.CANCELLED)) {
            throw new BookingException("Booking already cancelled");
        }
        seat.cancelBooking(clock);
        this.status = BookingStatus.CANCELLED;
    }

    public BookingDto toDto() {
        return new BookingDto(
                this.id,
                this.status.name(),
                this.seat.getId()
        );
    }
}
