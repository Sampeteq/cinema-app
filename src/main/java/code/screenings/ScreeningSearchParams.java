package code.screenings;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PACKAGE)
class ScreeningSearchParams {

    public final UUID filmId;

    public final ScreeningDate date;
}
