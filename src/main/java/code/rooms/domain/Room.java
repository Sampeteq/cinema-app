package code.rooms.domain;

import code.screenings.domain.Screening;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ROOMS")
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

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private final List<Screening> screenings = new ArrayList<>();

    protected Room() {}

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

    public void addScreening(Screening newScreening) {
        screenings.add(newScreening);
    }
}
