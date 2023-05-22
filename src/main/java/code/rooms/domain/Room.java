package code.rooms.domain;

import code.screenings.domain.Screening;
import code.screenings.domain.exceptions.ScreeningCollisionException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ROOMS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode(of = "id")
@Getter
@ToString
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customId;

    private int rowsQuantity;

    private int seatsInOneRowQuantity;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Screening> screenings = new ArrayList<>();

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
