package code.bookings.domain;

import code.shared.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "booking_film")
@Table(name = "bookings_films")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id")
public class Film {

    @Id
    private Long id;

    private String title;

    @OneToMany(mappedBy = "film", cascade = CascadeType.ALL)
    private final Set<Screening> screenings = new HashSet<>();

    private Film(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public static Film create(Long id, String title) {
        return new Film(id, title);
    }

    public void addFilm(Screening screening) {
        this.screenings.add(screening);
    }

    public void addSeatsToScreening(Seat seat, Long screeningId) {
        var foundScreening = this
                .screenings
                .stream()
                .filter(screening -> screening.getId().equals(screeningId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Screening"));
        foundScreening.addSeat(seat);
    }
}
