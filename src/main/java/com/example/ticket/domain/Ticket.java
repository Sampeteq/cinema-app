package com.example.ticket.domain;

import com.example.ticket.domain.dto.TicketDTO;
import com.example.ticket.domain.exception.TooLateToCancelTicketReservationException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.example.ticket.domain.TicketValues.TICKET_BASIC_PRIZE;

@Entity
@Table(name = "TICKETS")
@EqualsAndHashCode(of = "uuid")
@ToString
class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid = UUID.randomUUID();

    private String firstName;

    private String lastName;

    private BigDecimal prize = TICKET_BASIC_PRIZE;

    private TicketStatus status = TicketStatus.OPEN;

    @Getter
    private Long screeningId;

    protected Ticket() {
    }

    Ticket(String firstName, String lastName, Long screeningId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.screeningId = screeningId;
    }

    boolean isAlreadyCancelled() {
        return this.status.equals(TicketStatus.CANCELLED);
    }

    void cancel(LocalDateTime screeningDate, Clock clock) {
        var differenceBetweenCurrentDateAndScreeningOne= Duration
                .between(LocalDateTime.now(clock), screeningDate)
                .toHours();
        if (differenceBetweenCurrentDateAndScreeningOne < 24) {
            throw new TooLateToCancelTicketReservationException();
        }
        this.status = TicketStatus.CANCELLED;
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
