package code.films.application.handlers;

import code.films.domain.FilmScreeningRoom;
import code.films.domain.exceptions.FilmScreeningRoomCustomIdAlreadyExistsException;
import code.films.infrastructure.db.FilmScreeningRoomRepository;
import code.films.application.commands.FilmScreeningRoomCreateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FilmScreeningRoomCreateHandler {

    private final FilmScreeningRoomRepository roomRepository;

    public void handle(FilmScreeningRoomCreateCommand command) {
        if (roomRepository.existsByCustomId(command.customId())) {
            throw new FilmScreeningRoomCustomIdAlreadyExistsException();
        }
        var screeningRoom = FilmScreeningRoom.create(
                command.customId(),
                command.rowsQuantity(),
                command.seatsQuantityInOneRow()
        );
       roomRepository.add(screeningRoom);
    }
}
