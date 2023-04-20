package code.films.domain;

import code.films.domain.exceptions.ScreeningCollisionException;
import lombok.*;

import javax.persistence.*;
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
