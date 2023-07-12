package code.bookings.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "bookings_room")
@Table(name = "bookings_rooms")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Room {

    @Id
    private Long id;

    private String customId;

    private Room(Long id, String customId) {
        this.id = id;
        this.customId = customId;
    }

    public static Room create(Long id, String customId) {
        return new Room(id, customId);
    }
}
