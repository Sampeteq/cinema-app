package code.catalog.domain;

import lombok.Getter;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "rooms")
@Getter
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customId;

    private int rowsNumber;

    private int rowSeatsNumber;

    protected Room() {}

    public Room(String customId, int rowsNumber, int rowSeatsNumber) {
        this.customId = customId;
        this.rowsNumber = rowsNumber;
        this.rowSeatsNumber = rowSeatsNumber;
    }
}
