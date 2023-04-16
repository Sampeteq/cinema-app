package code.films.domain.queries.handlers;

import code.films.domain.ScreeningReadOnlyRepository;
import code.films.domain.queries.SearchScreeningsQuery;
import code.films.infrastructure.rest.dto.ScreeningDto;
import code.films.infrastructure.rest.mappers.ScreeningMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class SearchScreeningQueryHandler {

    private final ScreeningReadOnlyRepository screeningReadOnlyRepository;

    private final ScreeningMapper screeningMapper;

    public List<ScreeningDto> handle(SearchScreeningsQuery query) {
        return screeningReadOnlyRepository
                .findBy(query)
                .stream()
                .map(screeningMapper::mapToDto)
                .toList();
    }
}
