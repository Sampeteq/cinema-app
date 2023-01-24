package code.rooms;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RoomConfig {

    @Bean
    RoomFacade roomFacade(RoomRepository roomRepository) {
        var roomFactory = new RoomFactory(roomRepository);
        var roomSearcher = new RoomSearcher(roomRepository);
        return new RoomFacade(roomFactory, roomSearcher);
    }
}
