package com.cinema.repertoire.application.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@RequiredArgsConstructor
@Slf4j
@Profile("prod")
class ScreeningSchedulerService {

    private final ScreeningEndService screeningEndService;

    /** 3600000ms - 1h */
    @Scheduled(fixedDelay = 3600000)
    void run() {
      screeningEndService.removeRoomsFromEndedScreenings();
    }
}
