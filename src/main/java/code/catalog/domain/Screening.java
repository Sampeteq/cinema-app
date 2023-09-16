package code.catalog.domain;

import lombok.Getter;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "screenings")
@Getter
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Film film;

    @ManyToOne(fetch = FetchType.LAZY)
    private Room room;

    protected Screening() {}

    private Screening(LocalDateTime date, LocalDateTime endDate, Film film, Room room) {
        this.date = date;
        this.endDate = endDate;
        this.film = film;
        this.room = room;
    }

    public static Screening create(LocalDateTime date, Film film, Room room) {
        var endDate = film.calculateScreeningEndDate(date);
        return new Screening(
                date,
                endDate,
                film,
                room
        );
    }

    public void removeRoom() {
        this.room = null;
    }
}
