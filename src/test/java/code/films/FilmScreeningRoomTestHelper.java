package code.films;

import code.films.domain.FilmScreeningRoom;

public class FilmScreeningRoomTestHelper {

    public static FilmScreeningRoom createRoom() {
        return FilmScreeningRoom.create(
                "1",
                10,
                15
        );
    }
}
