package code.catalog.domain;

import code.catalog.domain.exceptions.FilmYearOutOfRangeException;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "FILMS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Getter
@ToString(exclude = "screenings")
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String title;

    @Enumerated(EnumType.STRING)
    @NonNull
    private FilmCategory category;

    @NonNull
    private Integer year;

    @NonNull
    private Integer durationInMinutes;

    @OneToMany(mappedBy = "film", cascade = CascadeType.PERSIST)
    private final List<Screening> screenings = new ArrayList<>();

    public static Film create(
            String title,
            FilmCategory category,
            Integer year,
            Integer durationInMinutes
    ) {
        if (!isFilmYearCorrect(year)) {
            throw new FilmYearOutOfRangeException();
        }
        return new Film(
                title,
                category,
                year,
                durationInMinutes
        );
    }

    private static boolean isFilmYearCorrect(Integer year) {
        var currentYear = Year.now().getValue();
        return year == currentYear - 1 || year == currentYear || year == currentYear + 1;
    }

    public LocalDateTime calculateScreeningEndDate(LocalDateTime screeningDate) {
        return screeningDate.plusMinutes(this.durationInMinutes);
    }

    public void addScreening(Screening newScreening) {
        this.screenings.add(newScreening);
    }
}
