package code.screenings.domain;

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
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    private int rowNumber;

    private int number;

    private boolean isFree;

    @ManyToOne
    private Screening screening;

    protected Seat() {
    }

    private Seat(int rowNumber, int number, boolean isFree, Screening screening) {
        this.rowNumber = rowNumber;
        this.number = number;
        this.isFree = isFree;
        this.screening = screening;
    }

    public static Seat of(int rowNumber, int number, Screening screening) {
        return new Seat(
                rowNumber,
                number,
                true,
                screening
        );
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