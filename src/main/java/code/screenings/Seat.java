package code.screenings;

import code.screenings.dto.SeatDto;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "SEATS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(of = "id")
@ToString
class Seat {

    @Id
    @Getter
    private UUID id;

    private int rowNumber;

    private int number;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @ManyToOne
    private Screening screening;

    boolean isFree() {
        return this.status.equals(SeatStatus.FREE);
    }

    void changeStatus(SeatStatus newStatus) {
        this.status = newStatus;
    }

    SeatDto toDto() {
        return new SeatDto(
                this.id,
                this.rowNumber,
                this.number,
                this.status.name()
        );
    }
}