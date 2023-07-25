package code.bookings.infrastructure.rest;

import code.bookings.application.services.SeatReadService;
import code.catalog.application.dto.SeatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/seats")
@RequiredArgsConstructor
public class SeatRestController {

    private final SeatReadService seatReadService;

    @GetMapping
    public List<SeatDto> readSeats(@RequestParam Long screeningId) {
        return seatReadService.readSeatsByScreeningId(screeningId);
    }
}
