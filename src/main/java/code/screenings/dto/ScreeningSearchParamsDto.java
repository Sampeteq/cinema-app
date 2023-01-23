package code.screenings.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class ScreeningSearchParamsDto {

    private final UUID filmId;

    private final LocalDateTime screeningDate;
}
