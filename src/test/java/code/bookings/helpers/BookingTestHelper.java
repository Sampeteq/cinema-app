package code.bookings.helpers;

import code.catalog.application.dto.FilmCreateDto;
import code.catalog.application.dto.RoomCreateDto;
import code.catalog.application.dto.ScreeningCreateDto;
import code.catalog.domain.FilmCategory;

import java.time.LocalDateTime;
import java.time.Year;

public class BookingTestHelper {

    public static FilmCreateDto createFilmCreateDto() {
        var tile = "Title 1";
        var category = FilmCategory.COMEDY;
        var year = Year.now().getValue();
        var durationInMinutes = 100;
        return new FilmCreateDto(
                tile,
                category,
                year,
                durationInMinutes
        );
    }

    public static RoomCreateDto createRoomCreateDto() {
        var customId = "1";
        var rowsNumber = 10;
        var seatsInOneRowsNumber = 15;
        return new RoomCreateDto(
                customId,
                rowsNumber,
                seatsInOneRowsNumber
        );
    }

    public static ScreeningCreateDto createScreeningCrateDto() {
        var date = LocalDateTime.of(2023, 10, 1, 16, 30);
        var filmId = 1L;
        return new ScreeningCreateDto(
                date,
                filmId
        );
    }

    public static ScreeningCreateDto createScreeningCrateDto(LocalDateTime date) {
        var filmId = 1L;
        return new ScreeningCreateDto(
                date,
                filmId
        );
    }
}
