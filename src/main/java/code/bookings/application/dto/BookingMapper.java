package code.bookings.application.dto;

import code.bookings.domain.Booking;
import code.screenings.domain.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface BookingMapper {

    @Mapping(target = "screening", source = "seat", qualifiedByName = "mapToBookingScreeningDto")
    BookingDto mapToDto(Booking booking);

    @Mapping(target = "seat", source = "seat", qualifiedByName = "seatToId")
    List<BookingDto> mapToDto(List<Booking> booking);

    @Named("mapToBookingScreeningDto")
    static BookingScreeningDto mapToBookingScreeningDto(Seat seat) {
        var bookingSeatDto = new BookingSeatDto(
                seat.getRowNumber(),
                seat.getNumber()
        );
        var filmTitle = seat.getScreening().getFilm().getTitle();
        var screeningDate = seat.getScreening().getDate();
        var roomNumber = seat.getScreening().getRoom().getCustomId();
        return new BookingScreeningDto(filmTitle, screeningDate, roomNumber, bookingSeatDto);
    }
}
