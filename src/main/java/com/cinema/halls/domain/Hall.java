package com.cinema.halls.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString(exclude = "seats")
public class Hall {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    private List<Seat> seats;
}
