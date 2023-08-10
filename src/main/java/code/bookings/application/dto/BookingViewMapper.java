package code.bookings.application.dto;

import code.bookings.domain.BookingView;
import org.mapstruct.Mapper;

@Mapper
public interface BookingViewMapper {
    BookingViewDto mapToDto(BookingView bookingView);
}
