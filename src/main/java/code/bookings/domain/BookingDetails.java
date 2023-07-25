package code.bookings.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "bookings_details")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BookingDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filmTitle;

    private String roomCustomId;

    @OneToOne
    private Booking booking;

    private BookingDetails(
            String filmTitle,
            String roomCustomId,
            Booking booking
    ) {
        this.filmTitle = filmTitle;
        this.roomCustomId = roomCustomId;
        this.booking = booking;
    }

    public static BookingDetails create(
            String filmTitle,
            String roomCustomId,
            Booking booking
    ) {
        return new BookingDetails(
                filmTitle,
                roomCustomId,
                booking
        );
    }
}
