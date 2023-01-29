package code.rooms.infrastructure.rest;

import code.rooms.application.RoomFacade;
import code.rooms.application.dto.CreateRoomDto;
import code.rooms.application.dto.RoomDto;
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

    private final RoomFacade roomFacade;

    @PostMapping("/rooms")
    public RoomDto createRoom(@RequestBody @Valid CreateRoomDto dto) {
        return roomFacade.createRoom(dto);
    }

    @GetMapping("/rooms")
    public List<RoomDto> searchAllRooms() {
        return roomFacade.searchAllRooms();
    }
}
