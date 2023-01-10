package code.screenings;

import code.screenings.dto.FilmCategoryView;
import code.screenings.dto.FilmView;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "FILMS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(of = "id")
@Getter
@ToString
class Film {

    @Id
    private UUID id;

    private String title;

    @Enumerated(EnumType.STRING)
    private FilmCategory category;

    private int year;

    private int durationInMinutes;

    FilmView toView() {
        return new FilmView(
                this.id,
                this.title,
                FilmCategoryView.valueOf(this.category.name()),
                year,
                durationInMinutes
        );
    }
}
