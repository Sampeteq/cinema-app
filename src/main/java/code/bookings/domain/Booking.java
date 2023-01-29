package code.bookings.domain;

import code.bookings.application.dto.BookingDto;
import code.bookings.domain.exception.BookingException;
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

    private String firstName;

    private String lastName;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private UUID screeningId;

    private UUID seatId;

    private String username;

    public void changeStatus(BookingStatus newStatus) {
        if (newStatus.equals(BookingStatus.CANCELLED) && this.status.equals(BookingStatus.CANCELLED)) {
            throw new BookingException("Booking already cancelled");
        }
        this.status = newStatus;
    }

    public BookingDto toDto() {
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
