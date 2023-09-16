package com.cinema.bookings_views.infrastructure.rest;

import com.cinema.bookings_views.application.dto.BookingViewDto;
import com.cinema.bookings_views.application.services.BookingReadService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingViewController {

    private final BookingReadService bookingReadService;

    @GetMapping("/my")
    @SecurityRequirement(name = "basic")
    List<BookingViewDto> readAllBookings() {
        return bookingReadService.readAll();
    }

    @GetMapping("/my/{bookingId}")
    @SecurityRequirement(name = "basic")
    BookingViewDto readBookingById(@PathVariable Long bookingId) {
        return bookingReadService.readById(bookingId);
    }
}
