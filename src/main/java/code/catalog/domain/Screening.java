package code.catalog.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "screenings")
@EqualsAndHashCode(of = "id")
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

    @Override
    public String toString() {
        return "Screening{" +
                "id=" + id +
                ", date=" + date +
                ", end date=" + endDate +
                ", film=" + film.getId() +
                ", room=" + room.getId() +
                '}';
    }
}
