package code.bookings.domain;

import code.bookings.domain.exceptions.BookingAlreadyCancelledException;
import code.bookings.infrastructure.rest.dto.BookingDto;
import lombok.*;

import javax.persistence.*;
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
    private BookingSeat seat;

    private String username;

    public static Booking make(BookingSeat seat, String username) {
        seat.book();
        return new Booking(
                UUID.randomUUID(),
                BookingStatus.ACTIVE,
                seat,
                username
        );
    }

    public void cancel() {
        if (status.equals(BookingStatus.CANCELLED)) {
            throw new BookingAlreadyCancelledException();
        }
        seat.cancelBooking();
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
