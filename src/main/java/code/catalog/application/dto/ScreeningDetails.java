package code.catalog.application.dto;

import lombok.Getter;

@Getter
public class ScreeningDetails {
    private final String roomCustomId;
    private final String filmTitle;

    public ScreeningDetails(String roomCustomId, String filmTitle) {
        this.roomCustomId = roomCustomId;
        this.filmTitle = filmTitle;
    }
}

