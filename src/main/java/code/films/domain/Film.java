package code.films.domain;

import code.films.domain.exceptions.WrongFilmYearException;
import code.screenings.domain.Screening;
import code.screenings.domain.exceptions.ScreeningCollisionException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "FILMS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode(of = "id")
@Getter
@ToString
@With
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
                null,
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
