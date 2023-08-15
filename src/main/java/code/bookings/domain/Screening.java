package code.bookings.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "screening_id")
    private List<Seat> seats;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "screening_id")
    private List<Booking> bookings = new ArrayList<>();

    private Screening(Long id, LocalDateTime date, List<Seat> seats) {
        this.id = id;
        this.date = date;
        this.seats = seats;
    }

    public static Screening create(Long id, LocalDateTime date, List<Seat> seats) {
        return new Screening(id, date, seats);
    }

    public int timeToScreeningInHours(LocalDateTime currentDate) {
        return (int) Duration
                .between(currentDate, date)
                .abs()
                .toHours();
    }

    public boolean hasActiveBooking() {
        return this
                .bookings
                .stream()
                .anyMatch(booking -> booking.hasStatus(BookingStatus.ACTIVE));
    }

    public void addBooking(Booking booking) {
        this.bookings.add(booking);
    }
}
