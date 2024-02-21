package com.cinema;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class ClockFixtures {

    public static final ZoneOffset ZONE_OFFSET = ZoneOffset.UTC;

    public static final Instant CURRENT_DATE = LocalDateTime
            .of(2024, 1, 1, 0,0)
            .toInstant(ZONE_OFFSET);

    public static final Clock CLOCK = Clock.fixed(CURRENT_DATE, ZONE_OFFSET);
}
