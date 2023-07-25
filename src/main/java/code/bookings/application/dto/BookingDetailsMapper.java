package code.bookings.application.dto;

import code.bookings.domain.Booking;
import org.mapstruct.Mapper;

@Mapper
public interface BookingDetailsMapper {

    default BookingDetailsDto mapToDto(Booking booking) {
        var bookingDetails = booking.getBookingDetails();
        return new BookingDetailsDto(
                bookingDetails.getId(),
                bookingDetails.getBooking().getStatus(),
                bookingDetails.getFilmTitle(),
                booking.getScreening().getDate(),
                bookingDetails.getRoomCustomId(),
                booking.getSeat().getRowNumber(),
                booking.getSeat().getNumber()
        );
    }
}
