package com.cinema;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class ClockFixtures {
    public static final ZoneOffset ZONE_OFFSET = ZoneOffset.UTC;

    public static final LocalDateTime CURRENT_DATE = LocalDateTime.of(2024, 1, 1, 0, 0);

    public static final Instant INSTANT = CURRENT_DATE.toInstant(ZONE_OFFSET);
}
