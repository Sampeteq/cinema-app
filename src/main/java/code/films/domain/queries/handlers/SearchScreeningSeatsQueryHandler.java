package code.films.domain.queries.handlers;

import code.bookings.domain.Seat;
import code.bookings.domain.SeatRepository;
import code.bookings.infrastructure.rest.dto.SeatDto;
import code.films.domain.queries.SearchScreeningSeatsQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SearchScreeningSeatsQueryHandler {

    private final SeatRepository seatRepository;

    @Transactional(readOnly = true)
    public List<SeatDto> handle(SearchScreeningSeatsQuery query) {
        return seatRepository
                .findByScreening_Id(query.screeningId())
                .stream()
                .map(Seat::toDto)
                .toList();
    }
}
