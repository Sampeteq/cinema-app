package code.films.domain.queries.handlers;

import code.films.infrastructure.rest.dto.ScreeningDto;
import code.films.domain.queries.SearchScreeningQuery;
import code.bookings.application.dto.SeatDto;
import code.films.infrastructure.rest.mappers.ScreeningMapper;
import code.films.domain.ScreeningRepository;
import code.bookings.domain.Seat;
import code.bookings.domain.SeatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class SearchScreeningQueryHandler {

    private final ScreeningRepository screeningRepository;

    private final SeatRepository seatRepository;

    private final ScreeningMapper screeningMapper;

    public List<ScreeningDto> searchScreeningsBy(SearchScreeningQuery params) {
        return screeningRepository
                .findBy(params)
                .stream()
                .map(screeningMapper::mapToDto)
                .toList();
    }

    public List<SeatDto> searchSeats(UUID screeningId) {
        return seatRepository
                .findByScreening_Id(screeningId)
                .stream()
                .map(Seat::toDto)
                .toList();
    }
}
