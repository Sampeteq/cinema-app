package code.bookings.infrastructure.rest;

import code.bookings.application.dto.BookingMakeDto;
import code.bookings.application.dto.BookingViewDto;
import code.bookings.application.services.BookingFacade;
import code.catalog.application.dto.SeatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
class BookingController {

    private final BookingFacade bookingFacade;

    @PostMapping
    void makeBooking(@RequestBody @Valid BookingMakeDto dto) {
        bookingFacade.makeBooking(dto);
    }

    @PostMapping("/{bookingId}/cancel")
    void cancelBooking(@PathVariable Long bookingId) {
        bookingFacade.cancelBooking(bookingId);
    }

    @GetMapping("/my")
    List<BookingViewDto> readAllBookings() {
        return bookingFacade.readAllBookings();
    }

    @GetMapping("/my/{bookingId}")
    BookingViewDto readBookingById(@PathVariable Long bookingId) {
        return bookingFacade.readBookingById(bookingId);
    }

    @GetMapping("/seats")
    List<SeatDto> readSeatsByScreeningId(@RequestParam Long screeningId) {
        return bookingFacade.readSeatsByScreeningId(screeningId);
    }
}

