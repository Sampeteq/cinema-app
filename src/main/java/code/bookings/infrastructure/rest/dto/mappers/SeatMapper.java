package code.bookings.infrastructure.rest.dto.mappers;

import code.bookings.domain.Seat;
import code.bookings.infrastructure.rest.dto.SeatDto;
import org.mapstruct.Mapper;

@Mapper
public interface SeatMapper {
    SeatDto toDto(Seat seat);
}
