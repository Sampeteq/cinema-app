package com.example.ticket.domain;

import com.example.ticket.domain.dto.TicketDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "TICKETS")
@EqualsAndHashCode(of = "id")
@ToString
class Ticket {

    @Id
    private final UUID id = UUID.randomUUID();

    private String firstName;

    private String lastName;

    private BigDecimal prize = TicketValues.TICKET_BASIC_PRIZE;

    private TicketStatus status = TicketStatus.OPEN;

    @Getter
    private UUID screeningId;

    protected Ticket() {
    }

    Ticket(String firstName, String lastName, UUID screeningId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.screeningId = screeningId;
    }

    void applyDiscount(TicketDiscountPolicy policy) {
        this.prize = policy.calculatePrize(this.prize);
    }

    void cancel() {
        this.status = TicketStatus.CANCELLED;
    }

    TicketDTO toDTO() {
        return TicketDTO
                .builder()
                .ticketId(this.id)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .prize(this.prize)
                .status(this.status)
                .screeningId(this.screeningId)
                .build();
    }
}
