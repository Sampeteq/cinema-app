package code.bookings.application.dto;

import code.bookings.domain.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface BookingMapper {
    default BookingDto mapToDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStatus(),
                booking.getSeat().getScreening().getFilm().getTitle(),
                booking.getSeat().getScreening().getDate(),
                booking.getSeat().getScreening().getRoom().getCustomId(),
                booking.getSeat().getRowNumber(),
                booking.getSeat().getNumber()
        );
    }

    @Mapping(target = "seat", source = "seat", qualifiedByName = "seatToId")
    List<BookingDto> mapToDto(List<Booking> booking);
}
