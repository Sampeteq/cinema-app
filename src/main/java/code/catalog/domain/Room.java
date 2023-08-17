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

@Entity
@Table(name = "rooms")
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
}
