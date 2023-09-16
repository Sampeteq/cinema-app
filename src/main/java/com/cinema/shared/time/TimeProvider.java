package com.cinema.shared.time;

import java.time.LocalDateTime;

public interface TimeProvider {
    LocalDateTime getCurrentDate();
}
