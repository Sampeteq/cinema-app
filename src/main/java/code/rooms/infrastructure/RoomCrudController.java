package code.rooms.infrastructure;

import code.rooms.RoomFacade;
import code.rooms.dto.CreateRoomDto;
import code.rooms.dto.RoomDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
class RoomCrudController {

    private final RoomFacade roomFacade;

    @PostMapping("/rooms")
    RoomDto createRoom(
            @RequestBody
            @Valid
            CreateRoomDto dto
    ) {
        return roomFacade.createRoom(dto);
    }

    @GetMapping("/rooms")
    List<RoomDto> searchAllRooms() {
        return roomFacade.searchAllRooms();
    }
}
