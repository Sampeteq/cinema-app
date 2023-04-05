package code.films.applications.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ScreeningSearchParams {

    public final UUID filmId;

    public final LocalDateTime date;
}
