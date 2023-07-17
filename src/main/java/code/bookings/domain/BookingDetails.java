package code.bookings.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings_details")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BookingDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filmTitle;

    private LocalDateTime screeningDate;

    private String roomCustomId;

    private int seatRowNumber;

    private int seatNumber;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    private BookingDetails(
            String filmTitle,
            LocalDateTime screeningDate,
            String roomCustomId,
            int seatRowNumber,
            int seatNumber,
            Booking booking
    ) {
        this.filmTitle = filmTitle;
        this.screeningDate = screeningDate;
        this.roomCustomId = roomCustomId;
        this.seatRowNumber = seatRowNumber;
        this.seatNumber = seatNumber;
        this.booking = booking;
    }

    public static BookingDetails create(
            String filmTitle,
            LocalDateTime date,
            String roomCustomId,
            int seatRowNumber,
            int seatNumber,
            Booking booking
    ) {
        return new BookingDetails(
                filmTitle,
                date,
                roomCustomId,
                seatRowNumber,
                seatNumber,
                booking
        );
    }
}
