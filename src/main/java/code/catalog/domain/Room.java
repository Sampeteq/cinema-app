package code.catalog.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rooms")
@EqualsAndHashCode(of = "id")
@Getter
@ToString
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customId;

    private int rowsNumber;

    private int rowSeatsNumber;

    protected Room() {}

    private Room(String customId, int rowsNumber, int rowSeatsNumber) {
        this.customId = customId;
        this.rowsNumber = rowsNumber;
        this.rowSeatsNumber = rowSeatsNumber;
    }

    public static Room create(String customId, int rowsNumber, int rowsSeatsNumber) {
        return new Room(
                customId,
                rowsNumber,
                rowsSeatsNumber
        );
    }
}
