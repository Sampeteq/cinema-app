package code.screenings.application.queries;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ScreeningReadQuery(String filmTitle, LocalDateTime date) {}
