package code.bookings.domain.client.dto.mappers;

import code.bookings.domain.Booking;
import code.bookings.domain.BookingSeat;
import code.bookings.domain.client.dto.BookingDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.UUID;

@Mapper
public interface BookingMapper {

    @Mapping(target = "seatId", source = "seat", qualifiedByName = "seatToUuid")
    BookingDto mapToDto(Booking booking);

    @Mapping(target = "seatId", source = "seat", qualifiedByName = "seatToUuid")
    List<BookingDto> mapToDto(List<Booking> booking);

    @Named("seatToUuid")
    static UUID seatToUuid(BookingSeat seat) {
        return seat.getId();
    }
}
