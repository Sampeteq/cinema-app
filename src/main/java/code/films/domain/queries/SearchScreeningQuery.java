package code.films.domain.queries;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SearchScreeningQuery {

    public final UUID filmId;

    public final LocalDateTime date;
}
