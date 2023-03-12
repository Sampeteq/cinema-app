package code.films.domain;

import code.films.application.dto.SeatDto;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringExclude;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "SEATS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Getter
@ToString
public class Seat {

    @Id
    @Getter
    private UUID id;

    private int rowNumber;

    private int number;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @ManyToOne
    private Screening screening;

    public boolean isFree() {
        return status.equals(SeatStatus.FREE);
    }

    public void changeStatus(SeatStatus newStatus) {
        status = newStatus;
    }

    public SeatDto toDto() {
        return new SeatDto(
                id,
                rowNumber,
                number,
                status.name()
        );
    }
}