package code.bookings;

import code.screenings.*;
import com.google.common.eventbus.EventBus;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
@AllArgsConstructor
class BookingConfig {

    @Bean
    BookingFacade filmFacade(
            BookingRepository bookingRepository,
            ScreeningFacade screeningFacade,
            EventBus eventBus
    ) {
        var booker = new Booker(bookingRepository, screeningFacade, eventBus);
        var bookingSearcher = new BookingSearcher(bookingRepository);
        return new BookingFacade(booker, bookingSearcher);
    }
}
