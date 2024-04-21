package com.cinema.tickets.infrastructure.db;

import com.cinema.halls.infrastructure.db.JpaSeat;
import com.cinema.screenings.infrastructure.db.JpaScreening;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Entity
@Table(name = "ticket")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude = "screening")
public class JpaTicket {
    @Id
    private UUID id;
    @ManyToOne
    private JpaScreening screening;
    @Embedded
    private JpaSeat seat;
    private UUID userId;
    @Version
    private int version;
}