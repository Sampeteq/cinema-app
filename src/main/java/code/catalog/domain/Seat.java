package code.catalog.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Clock;

@Entity
@Table(name = "SEATS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Getter
@ToString(exclude = {"screening"})
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    private int rowNumber;

    private int number;

    private boolean isFree;

    @ManyToOne(fetch = FetchType.LAZY)
    private Screening screening;

    public static Seat create(int rowNumber, int number) {
        Long id = null;
        Screening screening = null;
        final var isFree = true;
        return new Seat(
                id,
                rowNumber,
                number,
                isFree,
                screening
        );
    }

    public void assignScreening(Screening screening) {
        this.screening = screening;
    }

    public int timeToScreeningInHours(Clock clock) {
        return this.screening.timeToScreeningStartInHours(clock);
    }

    public void makeNotFree() {
        this.isFree = false;
    }

    public void makeFree() {
        this.isFree = true;
    }
}