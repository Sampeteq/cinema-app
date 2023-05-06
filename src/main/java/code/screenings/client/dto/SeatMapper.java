package code.screenings.client.dto;

import code.screenings.domain.Seat;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface SeatMapper {
    SeatDto toDto(Seat seat);
    List<SeatDto> toDto(List<Seat> seat);
}
