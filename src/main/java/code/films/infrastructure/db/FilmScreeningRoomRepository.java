package code.films.infrastructure.db;

import code.films.domain.FilmScreeningRoom;

import java.util.List;
import java.util.Optional;

public interface FilmScreeningRoomRepository {

    FilmScreeningRoom add(FilmScreeningRoom room);

    Optional<FilmScreeningRoom> readById(Long roomId);

    List<FilmScreeningRoom> readAll();

    boolean existsByCustomId(String customId);
}
