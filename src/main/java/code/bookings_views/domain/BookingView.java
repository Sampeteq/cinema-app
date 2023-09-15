package code.bookings_views.domain;

import code.bookings.domain.BookingStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
