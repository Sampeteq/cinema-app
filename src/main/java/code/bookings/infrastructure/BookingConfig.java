package code.bookings.infrastructure;

import code.bookings.application.Booker;
import code.bookings.application.BookingFacade;
import code.bookings.application.BookingSearcher;
import code.bookings.domain.BookingRepository;
import code.screenings.application.ScreeningFacade;
import com.google.common.eventbus.EventBus;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class BookingConfig {

    @Bean
    public BookingFacade filmFacade(
            BookingRepository bookingRepository,
            ScreeningFacade screeningFacade,
            EventBus eventBus
    ) {
        var booker = new Booker(bookingRepository, screeningFacade, eventBus);
        var bookingSearcher = new BookingSearcher(bookingRepository);
        return new BookingFacade(booker, bookingSearcher);
    }
}
