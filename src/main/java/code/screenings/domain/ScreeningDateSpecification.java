package code.screenings.domain;

import java.time.LocalDateTime;

public sealed interface ScreeningDateSpecification permits CurrentOrNextOneYearScreeningDateSpecification {
    boolean isSatisfyBy(LocalDateTime date);
}

