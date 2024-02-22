package com.cinema.screenings;

import com.cinema.screenings.exceptions.ScreeningNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScreeningService {

    private final ScreeningFactory screeningFactory;
    private final ScreeningRepository screeningRepository;

    @Transactional
    public Screening addScreening(Screening screening) {
        log.info("Screening:{}", screening);
        screeningFactory.validateScreening(screening);
        var addedScreening = screeningRepository.save(screening);
        log.info("Screening added:{}", addedScreening);
        return addedScreening;
    }

    public void deleteScreening(Long id) {
        log.info("Screening id:{}", id);
        var screening = screeningRepository
                .findById(id)
                .orElseThrow(ScreeningNotFoundException::new);
        screeningRepository.delete(screening);
    }

    public Screening getScreeningById(Long screeningId) {
        log.info("Screening id:{}", screeningId);
        return screeningRepository
                .findById(screeningId)
                .orElseThrow(ScreeningNotFoundException::new);
    }

    public List<Screening> getAllScreenings() {
        return screeningRepository.findAll();
    }

    public List<Screening> getScreeningsByDate(LocalDate date) {
        return screeningRepository.findScreeningsByDateBetween(
                date.atStartOfDay(),
                date.atStartOfDay().plusHours(23).plusMinutes(59)
        );
    }
}
