package code.rooms.infrastructure;

import code.rooms.application.RoomFacade;
import code.rooms.application.RoomFactory;
import code.rooms.application.RoomSearcher;
import code.rooms.domain.RoomRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoomConfig {

    @Bean
    public RoomFacade roomFacade(RoomRepository roomRepository) {
        var roomFactory = new RoomFactory(roomRepository);
        var roomSearcher = new RoomSearcher(roomRepository);
        return new RoomFacade(roomFactory, roomSearcher);
    }
}
