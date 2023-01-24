package code.rooms;

import code.utils.ScreeningTestHelper;
import code.utils.SpringIntegrationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static code.utils.ScreeningTestHelper.createScreeningRoomDto;
import static code.utils.WebTestHelper.toJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RoomsCrudTests extends SpringIntegrationTests {

    @Autowired
    private ScreeningTestHelper screeningTestHelper;

    @Test
    @WithMockUser(authorities = "ADMIN")
    void should_create_screening_room() throws Exception {
        //given
        var dto = createScreeningRoomDto();

        //when
        var result = mockMvc.perform(
                post("/rooms")
                        .content(toJson(dto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk());
        mockMvc.perform(
                        get("/rooms")
                )
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].number").value(dto.number()))
                .andExpect(jsonPath("$[0].rowsQuantity").value(dto.rowsQuantity()))
                .andExpect(jsonPath("$[0].seatsInOneRowQuantity").value(dto.seatsQuantityInOneRow()))
                .andExpect(jsonPath("$[0].seatsQuantity").value(dto.rowsQuantity() * dto.seatsQuantityInOneRow()));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void should_throw_exception_when_room_number_is_not_unique() throws Exception {
        //given
        var room = screeningTestHelper.createScreeningRoom();
        var dto = createScreeningRoomDto().withNumber(room.number());

        //when
        var result = mockMvc.perform(
                post("/rooms")
                        .content(toJson(dto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        "Screening room already exists: " + dto.number()
                ));
    }
}
