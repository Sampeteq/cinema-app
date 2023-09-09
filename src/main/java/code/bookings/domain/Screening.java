package code.bookings.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "booking_screening")
@Table(name = "bookings_screenings")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id")
@ToString(exclude = "seats")
public class Screening {

    @Id
    private Long id;

    private LocalDateTime date;

    @OneToMany(mappedBy = "screening", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Seat> seats;

    private Screening(Long id, LocalDateTime date, List<Seat> seats) {
        this.id = id;
        this.date = date;
        this.seats = seats;
    }

    public static Screening create(Long id, LocalDateTime date, List<Seat> seats) {
        var screening = new Screening(id, date, seats);
        seats.forEach(seat -> seat.assignScreening(screening));
        return screening;
    }

    public long timeToScreeningInHours(LocalDateTime currentDate) {
        return Duration
                .between(currentDate, date)
                .abs()
                .toHours();
    }
}
