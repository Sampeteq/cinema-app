package code.films.domain;

import code.films.domain.exceptions.ScreeningCollisionException;
import code.films.domain.exceptions.WrongFilmYearException;
import lombok.*;

import javax.persistence.*;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "FILMS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@Getter
@ToString(exclude = "screenings")
@With
public class Film {

    @Id
    private UUID id;

    private String title;

    @Enumerated(EnumType.STRING)
    private FilmCategory category;

    private int year;

    private int durationInMinutes;

    @OneToMany(mappedBy = "film", cascade = CascadeType.ALL)
    private List<Screening> screenings;

    public static Film create(FilmBuilder filmBuilder) {
        var fromBuilder = filmBuilder.build();
        if (!isFilmYearCorrect(filmBuilder.year)) {
            throw new WrongFilmYearException();
        }
        return new Film(
                UUID.randomUUID(),
                fromBuilder.title,
                fromBuilder.category,
                fromBuilder.year,
                fromBuilder.durationInMinutes,
                new ArrayList<>()
        );
    }

    private static boolean isFilmYearCorrect(Integer year) {
        var CURRENT_YEAR = Year.now().getValue();
        return year == CURRENT_YEAR - 1 || year == CURRENT_YEAR || year == CURRENT_YEAR + 1;
    }

    public void addScreening(Screening newScreening) {
        var isScreeningsCollision = screenings
                .stream()
                .anyMatch(screening -> screening.isCollisionWith(newScreening));
        if (isScreeningsCollision) {
            throw new ScreeningCollisionException();
        }
        screenings.add(newScreening);
    }
}
