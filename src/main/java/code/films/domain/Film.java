package code.films.domain;

import code.films.domain.exceptions.TimeAndRoomCollisionException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "FILMS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Getter
@ToString
public class Film {

    @Id
    private UUID id;

    private String title;

    @Enumerated(EnumType.STRING)
    private FilmCategory category;

    private int year;

    private int durationInMinutes;

    @OneToMany(mappedBy = "film", cascade = CascadeType.PERSIST)
    private List<Screening> screenings;

    public void addScreening(Screening newScreening) {
        var isScreeningsCollision = screenings
                .stream()
                .filter(screening -> screening.hasRoom(newScreening.getRoom()))
                .anyMatch(screening -> screening.isTimeCollision(
                        newScreening.getDate(),
                        newScreening.finishDate()
                        )
                );
        if (isScreeningsCollision) {
            throw new TimeAndRoomCollisionException();
        }
        screenings.add(newScreening);
    }
}
