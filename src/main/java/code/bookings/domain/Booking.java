package code.bookings.domain;

import code.bookings.application.dto.BookingDto;
import code.bookings.domain.exceptions.BookingException;
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

    private UUID screeningId;

    private UUID seatId;

    private String username;

    public void cancel() {
        if (this.status.equals(BookingStatus.CANCELLED)) {
            throw new BookingException("Booking already cancelled");
        }
        this.status = BookingStatus.CANCELLED;
    }

    public BookingDto toDto() {
        return new BookingDto(
                this.id,
                this.status.name(),
                this.screeningId,
                this.seatId
        );
    }
}
