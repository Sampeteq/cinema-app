package com.cinema.tickets_views.domain;

import com.cinema.tickets.domain.TicketStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets_views")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class TicketView {

    @Id
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private TicketStatus status;

    private String filmTitle;

    private LocalDateTime screeningDate;

    private String roomCustomId;

    private Integer seatRowNumber;

    private Integer seatNumber;

    private Long userId;

    public void changeStatus(TicketStatus status) {
        this.status = status;
    }
}
