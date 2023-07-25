package code.catalog.application.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScreeningDetails {
    private final Long screeningId;
    private final LocalDateTime screeningDate;
    private final Long roomId;
    private final String roomCustomId;
    private final Long filmId;
    private final String filmTitle;

    public ScreeningDetails(
            Long screeningId,
            LocalDateTime screeningDate,
            Long roomId,
            String roomCustomId,
            Long filmId,
            String filmTitle
    ) {
        this.screeningId = screeningId;
        this.screeningDate = screeningDate;
        this.roomId = roomId;
        this.roomCustomId = roomCustomId;
        this.filmId = filmId;
        this.filmTitle = filmTitle;
    }
}

