package code.films.application.handlers;

import code.films.application.dto.FilmScreeningRoomDto;
import code.films.application.dto.FilmScreeningRoomMapper;
import code.films.infrastructure.db.FilmScreeningRoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class FilmScreeningRoomReadHandler {

    private final FilmScreeningRoomRepository roomRepository;
    private final FilmScreeningRoomMapper roomMapper;

    public List<FilmScreeningRoomDto> handle() {
        return roomRepository
                .readAll()
                .stream()
                .map(roomMapper::mapToDto)
                .toList();
    }
}
