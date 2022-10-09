package com.example.ticket.domain;

import com.example.ticket.domain.dto.TicketDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.function.Function;

import static com.example.ticket.domain.TicketValues.TICKET_BASIC_PRIZE;

@Entity
@Table(name = "TICKETS")
@EqualsAndHashCode(of = "uuid")
@ToString
class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
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

    void applyDiscount(Function<BigDecimal, BigDecimal> discount) {
        var difference = discount.apply(this.prize);
        if (difference.compareTo(this.prize) >= 0) {
            throw new IllegalArgumentException("A discount difference cannot be bigger or equal to basic price");
        }
        this.prize = this.prize.subtract(difference);
    }

    boolean isAlreadyCancelled() {
        return this.status.equals(TicketStatus.CANCELLED);
    }

    void cancel() {
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
