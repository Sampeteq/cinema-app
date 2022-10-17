package code.screening;

import code.screening.dto.ScreeningDTO;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "SCREENINGS")
@EqualsAndHashCode(of = "uuid")
@ToString
class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid = UUID.randomUUID();

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "date"))
    private ScreeningDate date;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "min_Age"))
    private ScreeningAge minAge;

    private Long filmId;

    @ManyToOne
    private ScreeningRoom room;

    protected Screening() {
    }

    Screening(ScreeningDate date, ScreeningAge minAge, Long filmId, ScreeningRoom room) {
        this.date = date;
        this.minAge = minAge;
        this.filmId = filmId;
        this.room = room;
    }

    void decreaseFreeSeatsByOne() {
        this.room.decreaseFreeSeatsByOne(this.id);
    }

    ScreeningDTO toDTO() {
        return ScreeningDTO
                .builder()
                .id(this.id)
                .date(this.date.getValue())
                .freeSeats(this.room.getFreeSeats().getValue())
                .minAge(this.minAge.getValue())
                .filmId(this.filmId)
                .build();
    }
}
