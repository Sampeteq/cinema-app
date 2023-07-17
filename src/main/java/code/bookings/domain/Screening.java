package code.bookings.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity(name = "booking_screening")
@Table(name = "bookings_screenings")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id")
public class Screening {

    @Id
    private Long id;

    private LocalDateTime date;

    private Screening(Long id, LocalDateTime date) {
        this.id = id;
        this.date = date;
    }

    public static Screening create(Long id, LocalDateTime date) {
        return new Screening(id, date);
    }

    public int timeToScreeningInHours(LocalDateTime currentDate) {
        return (int) Duration
                .between(currentDate, date)
                .abs()
                .toHours();
    }
}
