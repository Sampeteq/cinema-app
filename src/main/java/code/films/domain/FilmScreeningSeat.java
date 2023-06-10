package code.films.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Clock;

@Entity
@Table(name = "SEATS")
@EqualsAndHashCode(of = "id")
@Getter
@ToString(exclude = {"screening"})
public class FilmScreeningSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    private int rowNumber;

    private int number;

    private boolean isFree;

    @ManyToOne
    private FilmScreening screening;

    protected FilmScreeningSeat() {
    }

    private FilmScreeningSeat(int rowNumber, int number, boolean isFree) {
        this.rowNumber = rowNumber;
        this.number = number;
        this.isFree = isFree;
    }

    public static FilmScreeningSeat of(int rowNumber, int number) {
        return new FilmScreeningSeat(
                rowNumber,
                number,
                true
        );
    }

    public void assignScreening(FilmScreening screening) {
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