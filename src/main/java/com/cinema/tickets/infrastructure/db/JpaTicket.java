package com.cinema.tickets.infrastructure.db;

import com.cinema.halls.infrastructure.db.JpaSeat;
import com.cinema.screenings.infrastructure.db.JpaScreening;
import com.cinema.users.infrastructure.db.JpaUser;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ticket")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude = "screening")
class JpaTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    private Long id;
    @ManyToOne
    private JpaScreening screening;
    @Embedded
    private JpaSeat seat;
    @ManyToOne
    private JpaUser user;
    @Version
    private int version;
}