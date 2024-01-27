package com.cinema.halls.ui;

import com.cinema.halls.domain.Hall;

import java.util.List;

public record HallsResponse(List<Hall> halls) {
}
