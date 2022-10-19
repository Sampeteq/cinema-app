package code.screening;

import code.reservation.exception.ScreeningTicketException;
import code.reservation.dto.TicketDTO;
import code.screening.exception.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Clock;
import java.util.UUID;

@Entity
@Table(name = "SCREENINGS_TICKETS")
@EqualsAndHashCode(of = "uuid")
@ToString
class ScreeningTicket {

    private static final BigDecimal TICKET_BASIC_PRIZE = new BigDecimal("10.0");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid = UUID.randomUUID();

    private String firstName;

    private String lastName;

    private BigDecimal prize = TICKET_BASIC_PRIZE;

    private ScreeningTicketStatus status = ScreeningTicketStatus.OPEN;

    @ManyToOne
    private Screening screening;

    protected ScreeningTicket() {
    }

    ScreeningTicket(String firstName, String lastName, Screening screening) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.screening = screening;
    }

    void reserve(Clock clock) {
        if (this.screening.differenceBetweenCurrentDateAndScreeningOneInHours(clock) < 24) {
            throw ScreeningTicketException.tooLateToReserve();
        }
        if (!this.screening.hasFreeSeats()) {
            throw ScreeningFreeSeatsException.noFreeSeats(this.screening.getId());
        }
        this.screening.decreaseFreeSeatsByOne();
    }

    void cancel(Clock clock) {
        if (this.status.equals(ScreeningTicketStatus.CANCELLED)) {
            throw ScreeningTicketException.alreadyCancelled(this.uuid);
        }
        if (this.screening.differenceBetweenCurrentDateAndScreeningOneInHours(clock) < 24) {
            throw ScreeningTicketException.tooLateToCancel();
        }
        this.status = ScreeningTicketStatus.CANCELLED;
    }

    TicketDTO toDTO() {
        return TicketDTO
                .builder()
                .ticketUuid(this.uuid)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .prize(this.prize)
                .status(this.status)
                .screeningId(this.screening.toDTO().id())
                .build();
    }
}
