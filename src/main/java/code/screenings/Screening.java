package code.screenings;

import code.rooms.dto.RoomDetails;
import code.screenings.dto.ScreeningDto;
import code.screenings.dto.SeatDto;
import lombok.*;

import javax.persistence.*;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "SCREENINGS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@ToString
class Screening {

    @Id
    @Getter
    private UUID id;

    private LocalDateTime date;

    private int minAge;

    @ManyToOne
    private Film film;

    private UUID roomId;

    @OneToMany(mappedBy = "screening", cascade = CascadeType.ALL)
    private List<Seat> seats;

    static Screening of(LocalDateTime date, int minAge, Film film, UUID roomId, RoomDetails roomDetails) {
        var screening = new Screening(
                UUID.randomUUID(),
                date,
                minAge,
                film,
                roomId,
                new ArrayList<>()
        );
        var seats = new ArrayList<Seat>();
        var rowNumber = 1;
        var seatNumber = 1;
        var helpCounter = 1;
        for (int i = 1; i <= roomDetails.rowsQuantity() * roomDetails.seatsInOneRowQuantity(); i++) {
            if (helpCounter > roomDetails.seatsInOneRowQuantity()) {
                rowNumber++;
                seatNumber = 1;
                helpCounter = 1;
            }
            var seat = new Seat(
                    UUID.randomUUID(),
                    rowNumber,
                    seatNumber++,
                    SeatStatus.FREE,
                    screening
            );
            seats.add(seat);
            helpCounter++;
        }
        screening.seats = seats;
        return screening;
    }

    Optional<Seat> getSeat(UUID seatId) {
        return this.seats
                .stream()
                .filter(seat -> seat.getId().equals(seatId))
                .findFirst();
    }

    int timeToScreeningStartInHours(Clock clock) {
        return (int) Duration
                .between(LocalDateTime.now(clock), this.date)
                .abs()
                .toHours();
    }

    boolean IsTimeCollision(LocalDateTime start, LocalDateTime finish) {
        var screeningFinishDate = this.date.plusMinutes(film.getDurationInMinutes());
        return screeningFinishDate.isAfter(start) && this.date.isBefore(finish);
    }

    ScreeningDto toDto() {
        return new ScreeningDto(
                this.id,
                this.date,
                this.minAge,
                this.film.getId(),
                this.roomId,
                (int) this.seats.stream().filter(Seat::isFree).count()
        );
    }

    List<SeatDto> seatsDtos() {
        return this
                .seats
                .stream()
                .map(Seat::toDto)
                .toList();
    }
}
