package code.shared;

import com.google.common.eventbus.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SharedConfig {

    @Bean
    EventBus eventBus() {
        return new EventBus();
    }
}
