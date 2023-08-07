package code.catalog.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "SCREENINGS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Getter
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private LocalDateTime date;

    @NonNull
    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @NonNull
    private Film film;

    @ManyToOne(fetch = FetchType.LAZY)
    @NonNull
    private Room room;

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
