package code.screenings;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PACKAGE)
class ScreeningSearchParams {

    final UUID filmId;

    final ScreeningDate date;
}
