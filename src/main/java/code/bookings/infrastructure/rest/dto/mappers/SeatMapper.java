package code.bookings.infrastructure.rest.dto.mappers;

import code.bookings.infrastructure.rest.dto.SeatDto;
import code.films.domain.Seat;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface SeatMapper {
    SeatDto toDto(Seat seat);
    List<SeatDto> toDto(List<Seat> seat);
}
