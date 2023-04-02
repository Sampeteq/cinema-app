package code.bookings.domain;

import code.bookings.domain.exceptions.TimeAndRoomCollisionException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "FILMS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Getter
@ToString
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
            throw new TimeAndRoomCollisionException();
        }
        screenings.add(newScreening);
    }
}
