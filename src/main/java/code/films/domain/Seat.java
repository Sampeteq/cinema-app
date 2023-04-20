package code.films.domain;

import code.bookings.client.commands.events.DecreasedFreeSeatsEvent;
import code.bookings.client.commands.events.IncreasedFreeSeatsEvent;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "SEATS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Getter
@ToString
public class Seat {

    @Id
    @Getter
    private UUID id;

    private int rowNumber;

    private int number;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @ManyToOne
    private Screening screening;

    public boolean isFree() {
        return status.equals(SeatStatus.FREE);
    }

    public void handleEvent(DecreasedFreeSeatsEvent event) {
        this.status = SeatStatus.BUSY;
    }

    public void handleEvent(IncreasedFreeSeatsEvent event) {
        this.status = SeatStatus.FREE;
    }
}