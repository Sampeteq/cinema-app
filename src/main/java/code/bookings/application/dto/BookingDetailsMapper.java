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
                booking.getSeat().getScreening().getDate(),
                bookingDetails.getRoomCustomId(),
                bookingDetails.getSeatRowNumber(),
                bookingDetails.getSeatNumber()
        );
    }
}
