package code.reservation;

import code.reservation.dto.ReserveScreeningTicketDTO;
import code.reservation.exception.ScreeningTicketException;
import code.reservation.dto.TicketDTO;
import code.screening.dto.ScreeningReservationData;
import code.screening.exception.*;
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

    private ScreeningTicket(String firstName, String lastName, Long screeningId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.screeningId = screeningId;
    }

    static ScreeningTicket reserve(
            ReserveScreeningTicketDTO reserveScreeningTicketDTO,
            ScreeningReservationData screeningReservationData,
            Clock clock
    ) {

        var differenceBetweenCurrentDateAndScreeningOne= Duration
                .between(LocalDateTime.now(clock), screeningReservationData.screeningDate())
                .abs()
                .toHours();
        if (differenceBetweenCurrentDateAndScreeningOne < 24) {
            throw ScreeningTicketException.tooLateToReserve();
        }
        if (screeningReservationData.screeningFreeSeats() == 0) {
            throw ScreeningFreeSeatsException.noFreeSeats(reserveScreeningTicketDTO.screeningId());
        }
        return new ScreeningTicket(
                reserveScreeningTicketDTO.firstName(),
                reserveScreeningTicketDTO.lastName(),
                reserveScreeningTicketDTO.screeningId()
        );
    }

    void cancel(LocalDateTime screeningDate, Clock clock) {
        if (this.status.equals(ScreeningTicketStatus.CANCELLED)) {
            throw ScreeningTicketException.alreadyCancelled(this.uuid);
        }
        var differenceBetweenCurrentDateAndScreeningOne= Duration
                .between(LocalDateTime.now(clock), screeningDate)
                .abs()
                .toHours();
        if (differenceBetweenCurrentDateAndScreeningOne < 24) {
            throw ScreeningTicketException.tooLateToReserve();
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
                .screeningId(this.screeningId)
                .build();
    }
}
