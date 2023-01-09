package code.screenings;

import code.screenings.dto.ScreeningDto;
import code.screenings.dto.SeatDto;
import code.screenings.exception.ScreeningRoomException;
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

    private LocalDateTime finishDate;

    private int minAge;

    @ManyToOne
    private Film film;

    @ManyToOne(cascade = CascadeType.ALL)
    private ScreeningRoom room;

    @OneToMany(mappedBy = "screening", cascade = CascadeType.ALL)
    private List<Seat> seats;

    static Screening of(LocalDateTime date, int minAge, Film film, ScreeningRoom room) {
        return new Screening(
                UUID.randomUUID(),
                date,
                date.plusMinutes(film.getDurationInMinutes()),
                minAge,
                film,
                room,
                new ArrayList<>()
        );
    }

    void addSeats(List<Seat> seats) {
        if (seats.size() > room.seatsQuantity()) {
            throw new ScreeningRoomException("Screening seats quantity cannot be bigger than room's one");
        }
        this.seats.addAll(seats);
        this.seats.forEach(seat -> seat.assignScreening(this));
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

    ScreeningDto toDTO() {
        return new ScreeningDto(
                this.id,
                this.date,
                this.minAge,
                this.film.toDTO().id(),
                this.room.toDTO().id(),
                (int) this.seats.stream().filter(Seat::isFree).count()
        );
    }

    public List<SeatDto> seatsDto() {
        return this
                .seats
                .stream()
                .map(Seat::toDTO)
                .toList();
    }
}
