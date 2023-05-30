package code.bookings.application.dto;

import code.bookings.domain.Booking;
import code.screenings.domain.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface BookingMapper {

    BookingDto mapToDto(Booking booking);

    @Mapping(target = "seat", source = "seat", qualifiedByName = "seatToId")
    List<BookingDto> mapToDto(List<Booking> booking);

    BookingSeatDto mapToDto(Seat seat);
}
