package com.cinema.bookings.application.services;

import com.cinema.bookings.application.dto.BookingMakeDto;
import com.cinema.catalog.application.dto.SeatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingFacade {

    private final BookingMakeService bookingMakeService;
    private final BookingCancelService bookingCancelService;
    private final SeatReadService seatReadService;

    public void makeBooking(BookingMakeDto dto) {
        bookingMakeService.makeBooking(dto);
    }

    public void cancelBooking(Long bookingId) {
        bookingCancelService.cancelBooking(bookingId);
    }

    public List<SeatDto> readSeatsByScreeningId(Long screeningId) {
        return seatReadService.readSeatsByScreeningId(screeningId);
    }
}
