package code.bookings.application.dto;

import code.bookings.domain.BookingDetails;
import org.mapstruct.Mapper;

@Mapper
public interface BookingDetailsMapper {

    default BookingDetailsDto mapToDto(BookingDetails bookingDetails) {
        return new BookingDetailsDto(
                bookingDetails.getId(),
                bookingDetails.getBooking().getStatus(),
                bookingDetails.getFilmTitle(),
                bookingDetails.getScreeningDate(),
                bookingDetails.getRoomCustomId(),
                bookingDetails.getSeatRowNumber(),
                bookingDetails.getSeatNumber()
        );
    }
}
