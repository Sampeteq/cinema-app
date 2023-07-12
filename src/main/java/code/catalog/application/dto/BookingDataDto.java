package code.catalog.application.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BookingDataDto {
    private final Long screeningId;
    private final LocalDateTime screeningDate;
    private final Long roomId;
    private final String roomCustomId;
    private final int seatRowNumber;
    private final int seatNumber;
    private final Long filmId;
    private final String filmTitle;

    public BookingDataDto(
            Long screeningId,
            LocalDateTime screeningDate,
            Long roomId,
            String roomCustomId,
            int seatRowNumber,
            int seatNumber,
            Long filmId,
            String filmTitle
    ) {
        this.screeningId = screeningId;
        this.screeningDate = screeningDate;
        this.roomId = roomId;
        this.roomCustomId = roomCustomId;
        this.seatRowNumber = seatRowNumber;
        this.seatNumber = seatNumber;
        this.filmId = filmId;
        this.filmTitle = filmTitle;
    }
}

