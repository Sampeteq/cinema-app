package com.cinema.bookings_views.application.dto;

import com.cinema.bookings_views.domain.BookingView;
import org.mapstruct.Mapper;

@Mapper
public interface BookingViewMapper {
    BookingViewDto mapToDto(BookingView bookingView);
}
