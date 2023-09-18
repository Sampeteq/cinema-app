package com.cinema.tickets.domain;

import com.cinema.tickets.domain.exceptions.TicketAlreadyExists;
import com.cinema.tickets.domain.exceptions.TicketBookTooLateException;
import com.cinema.tickets.domain.exceptions.TicketCancelTooLateException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "seats")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@Getter
@ToString(exclude = {"screening", "ticket"})
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rowNumber;

    private int number;

    private boolean isFree;

    @ManyToOne
    private Screening screening;

    @OneToOne(mappedBy = "seat", cascade = CascadeType.ALL)
    private Ticket ticket;

    private Seat(int rowNumber, int number, boolean isFree) {
        this.rowNumber = rowNumber;
        this.number = number;
        this.isFree = isFree;
    }

    public static Seat create(int rowNumber, int number) {
        final var isFree = true;
        return new Seat(
                rowNumber,
                number,
                isFree
        );
    }

    public void assignScreening(Screening screening) {
        this.screening = screening;
    }

    public boolean placedOn(int rowNumber, int seatNumber) {
        return this.rowNumber == rowNumber && this.number == seatNumber;
    }

    public void addTicket(LocalDateTime currentDate, Ticket ticket) {
        if (screening.timeToScreeningInHours(currentDate) < 1) {
            throw new TicketBookTooLateException();
        }
        if (this.ticket != null) {
            throw new TicketAlreadyExists();
        }
        this.ticket = ticket;
        this.isFree = false;
    }

    public void removeTicket(LocalDateTime currentDate) {
        if (this.screening.timeToScreeningInHours(currentDate) < 24) {
            throw new TicketCancelTooLateException();
        }
        this.ticket = null;
        this.isFree = true;
    }
}