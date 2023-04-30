package code.rooms.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "ROOMS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@Getter
@ToString
public class Room {

    @Id
    private UUID id;

    private String customId;

    private int rowsQuantity;

    private int seatsInOneRowQuantity;
}
