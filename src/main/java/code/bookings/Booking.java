package code.bookings;

import code.bookings.dto.BookingDto;
import code.bookings.exception.BookingException;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "BOOKINGS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(of = "id")
@Getter
@ToString
class Booking {

    @Id
    private UUID id;

    private String firstName;

    private String lastName;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private UUID screeningId;

    private UUID seatId;

    private String username;

    void changeStatus(BookingStatus newStatus) {
        if (newStatus.equals(BookingStatus.CANCELLED) && this.status.equals(BookingStatus.CANCELLED)) {
            throw new BookingException("Booking already cancelled");
        }
        this.status = newStatus;
    }

    BookingDto toDto() {
        return new BookingDto(
                this.id,
                this.firstName,
                this.lastName,
                this.status.name(),
                this.screeningId,
                this.seatId
        );
    }
}
