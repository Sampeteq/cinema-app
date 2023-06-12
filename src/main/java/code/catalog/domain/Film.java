package code.catalog.domain;

import code.catalog.domain.exceptions.FilmWrongYearException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

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
@EqualsAndHashCode(of = "id")
@Getter
@ToString
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
    private final List<Screening> screenings = new ArrayList<>();

    protected Film() {}

    private Film(String title, FilmCategory category, int year, int durationInMinutes) {
        this.title = title;
        this.category = category;
        this.year = year;
        this.durationInMinutes = durationInMinutes;
    }

    public static Film create(String title, FilmCategory category, int year, int durationInMinutes) {
        if (!isFilmYearCorrect(year)) {
            throw new FilmWrongYearException();
        }
        return new Film(
                title,
                category,
                year,
                durationInMinutes
        );
    }

    public void addScreening(Screening newScreening) {
        this.screenings.add(newScreening);
    }

    private static boolean isFilmYearCorrect(Integer year) {
        var CURRENT_YEAR = Year.now().getValue();
        return year == CURRENT_YEAR - 1 || year == CURRENT_YEAR || year == CURRENT_YEAR + 1;
    }
}
