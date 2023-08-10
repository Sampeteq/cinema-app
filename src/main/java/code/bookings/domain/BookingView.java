package code.bookings.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings_views")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class BookingView {

    @Id
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private BookingStatus status;

    private String filmTitle;

    private LocalDateTime screeningDate;

    private String roomCustomId;

    private Integer seatRowNumber;

    private Integer seatNumber;

    private Long userId;

    public void changeStatus(BookingStatus status) {
        this.status = status;
    }
}
