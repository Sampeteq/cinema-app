package code.films.domain.queries.handlers;

import code.films.domain.ScreeningReadOnlyRepository;
import code.films.domain.queries.GetScreeningsQuery;
import code.films.infrastructure.rest.dto.ScreeningDto;
import code.films.infrastructure.rest.mappers.ScreeningMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class GetScreeningHandler {

    private final ScreeningReadOnlyRepository screeningReadOnlyRepository;

    private final ScreeningMapper screeningMapper;

    public List<ScreeningDto> handle(GetScreeningsQuery query) {
        return screeningReadOnlyRepository
                .getBy(query)
                .stream()
                .map(screeningMapper::mapToDto)
                .toList();
    }
}
