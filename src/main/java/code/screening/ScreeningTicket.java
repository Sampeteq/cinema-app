package code.screening;

import code.screening.dto.TicketDTO;
import code.screening.exception.ScreeningTicketAlreadyCancelledException;
import code.screening.exception.TooLateToCancelScreeningTicketException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
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

    @Getter
    private Long screeningId;

    protected ScreeningTicket() {
    }

    ScreeningTicket(String firstName, String lastName, Long screeningId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.screeningId = screeningId;
    }

    void cancel(LocalDateTime screeningDate, Clock clock) {
        if (this.status.equals(ScreeningTicketStatus.CANCELLED)) {
            throw new ScreeningTicketAlreadyCancelledException(this.uuid);
        }
        var currentDate = LocalDateTime.now(clock);
        var differenceBetweenCurrentDateAndScreeningOne = Duration
                .between(screeningDate, currentDate)
                .abs()
                .toHours();
        if (differenceBetweenCurrentDateAndScreeningOne < 24) {
            throw new TooLateToCancelScreeningTicketException();
        }
        this.status = ScreeningTicketStatus.CANCELLED;
    }

    TicketDTO toDTO() {
        return TicketDTO
                .builder()
                .ticketId(this.id)
                .ticketUuid(this.uuid)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .prize(this.prize)
                .status(this.status)
                .screeningId(this.screeningId)
                .build();
    }
}
