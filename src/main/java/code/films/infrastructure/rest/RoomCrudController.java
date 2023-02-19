package code.films.infrastructure.rest;

import code.films.application.FilmFacade;
import code.films.application.dto.CreateRoomDto;
import code.films.application.dto.RoomDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
public class RoomCrudController {

    private final FilmFacade filmFacade;

    @PostMapping("/rooms")
    public RoomDto createRoom(@RequestBody @Valid CreateRoomDto dto) {
        return filmFacade.createRoom(dto);
    }

    @GetMapping("/rooms")
    public List<RoomDto> searchAllRooms() {
        return filmFacade.searchAllRooms();
    }
}
