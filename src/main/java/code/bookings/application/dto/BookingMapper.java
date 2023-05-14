package code.bookings.application.dto;

import code.bookings.domain.Booking;
import code.screenings.domain.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface BookingMapper {

    @Mapping(target = "seatId", source = "seat", qualifiedByName = "seatToId")
    BookingDto mapToDto(Booking booking);

    @Mapping(target = "seatId", source = "seat", qualifiedByName = "seatToId")
    List<BookingDto> mapToDto(List<Booking> booking);

    @Named("seatToId")
    static Long seatToUuid(Seat seat) {
        return seat.getId();
    }
}
