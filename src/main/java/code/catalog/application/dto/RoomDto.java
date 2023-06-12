package code.catalog.application.dto;

public record RoomDto(
        Long id,
        String customId,
        int rowsQuantity,
        int seatsInOneRowQuantity,
        int seatsQuantity
) {
}
