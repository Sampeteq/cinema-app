package code.films.infrastructure.db;

import code.films.domain.FilmScreeningRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FilmScreeningRoomSpringDataJpaRepository implements FilmScreeningRoomRepository {

    private final FilmScreeningRoomJpaRepository jpaRoomRepository;

    @Override
    public FilmScreeningRoom add(FilmScreeningRoom room) {
        return jpaRoomRepository.save(room);
    }

    @Override
    public Optional<FilmScreeningRoom> readById(Long roomId) {
        return jpaRoomRepository.findById(roomId);
    }

    @Override
    public List<FilmScreeningRoom> readAll() {
        return jpaRoomRepository.findAll();
    }

    @Override
    public boolean existsByCustomId(String customId) {
        return jpaRoomRepository.existsByCustomId(customId);
    }
}

interface FilmScreeningRoomJpaRepository extends JpaRepository<FilmScreeningRoom, Long> {
    boolean existsByCustomId(String customId);
}
