package com.example.ticket.domain;

import com.example.ticket.domain.dto.TicketDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.UUID;
import java.util.function.Function;

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

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "prize"))
    private Money prize = Money.of(TicketValues.TICKET_BASIC_PRIZE);

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

    void applyDiscount(Function<Money, Money> policy) {
        this.prize = policy.apply(this.prize);
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
