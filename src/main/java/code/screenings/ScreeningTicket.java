package code.screenings;

import code.screenings.dto.ScreeningTicketDto;
import lombok.*;

import javax.persistence.*;
import java.time.Clock;
import java.util.UUID;

@Entity
@Table(name = "SCREENINGS_TICKETS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(of = "id")
@ToString
class ScreeningTicket {

    @Id
    private UUID id;

    private String firstName;

    private String lastName;

    @OneToOne
    private ScreeningRoomSeat seat;

    void cancelSeatBooking(Clock clock) {
        this.seat.cancelBooking(clock);
    }

    ScreeningTicketDto toDTO() {
        return new ScreeningTicketDto(
                this.id,
                this.firstName,
                this.lastName,
                this.seat.toDTO()
        );
    }
}
