package code.films.application.dto;

public record FilmScreeningRoomDto(
        Long id,
        String customId,
        int rowsQuantity,
        int seatsInOneRowQuantity,
        int seatsQuantity
) {
}
