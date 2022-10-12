package code.screening.dto;

import lombok.Builder;

import javax.validation.constraints.Positive;

@Builder
public record AddScreeningRoomDTO(@Positive int number, @Positive int freeSeats) {
}
