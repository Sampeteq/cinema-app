package code.bookings_views.application.dto;

import code.bookings_views.application.dto.BookingViewDto;
import code.bookings_views.domain.BookingView;
import org.mapstruct.Mapper;

@Mapper
public interface BookingViewMapper {
    BookingViewDto mapToDto(BookingView bookingView);
}
