package code.catalog.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

import static java.util.stream.IntStream.rangeClosed;

@Entity
@Table(name = "ROOMS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Getter
@ToString
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customId;

    private int rowsQuantity;

    private int seatsInOneRowQuantity;

    private Room(String customId, int rowsQuantity, int seatsInOneRowQuantity) {
        this.customId = customId;
        this.rowsQuantity = rowsQuantity;
        this.seatsInOneRowQuantity = seatsInOneRowQuantity;
    }

    public static Room create(String customId, int rowsQuantity, int seatsInOneRowQuantity) {
        return new Room(
                customId,
                rowsQuantity,
                seatsInOneRowQuantity
        );
    }

    public List<Seat> createSeats() {
        return rangeClosed(1, this.rowsQuantity)
                .boxed()
                .flatMap(rowNumber -> rangeClosed(1, this.seatsInOneRowQuantity)
                        .mapToObj(seatNumber -> Seat.create(rowNumber, seatNumber))
                )
                .toList();
    }
}
