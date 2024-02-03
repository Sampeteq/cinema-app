package com.cinema.halls.ui;

import com.cinema.BaseIT;
import com.cinema.halls.HallFixture;
import com.cinema.halls.application.dto.CreateHallDto;
import com.cinema.halls.application.dto.CreateSeatDto;
import com.cinema.halls.domain.Hall;
import com.cinema.halls.domain.HallRepository;
import com.cinema.users.UserFixture;
import com.cinema.users.domain.User;
import com.cinema.users.domain.UserRepository;
import com.cinema.users.domain.UserRole;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.List;

import static com.cinema.halls.HallFixture.createHall;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;

class HallControllerIT extends BaseIT {

    private static final String HALL_ADMIN_ENDPOINT = "/admin/halls";

    @Autowired
    private HallRepository hallRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @SneakyThrows
    void hall_is_created() {
        //given
        var createHallDto = new CreateHallDto(
                List.of(
                        new CreateSeatDto(1, 1),
                        new CreateSeatDto(1, 2)
                )
        );
        var user = addUser();

        //when
        var responseSpec = webTestClient
                .post()
                .uri(HALL_ADMIN_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createHallDto)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange();

        //then
        responseSpec
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.seats[0].number").isEqualTo(createHallDto.seats().get(0).number())
                .jsonPath("$.seats[0].rowNumber").isEqualTo(createHallDto.seats().get(0).rowNumber())
                .jsonPath("$.seats[1].number").isEqualTo(createHallDto.seats().get(1).number())
                .jsonPath("$.seats[1].rowNumber").isEqualTo(createHallDto.seats().get(1).rowNumber());
    }

    @Test
    void hall_is_deleted() {
        //given
        var hall = addHall();
        var user = addUser();

        //when
        var responseSpec = webTestClient
                .delete()
                .uri(HALL_ADMIN_ENDPOINT + "/" + hall.getId())
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange();

        //then
        responseSpec.expectStatus().isNoContent();
        assertThat(hallRepository.findById(hall.getId())).isEmpty();
    }

    @Test
    void halls_are_gotten() {
        //given
        var hall = hallRepository.save(createHall());
        var user = addUser();

        //when
        var responseSpec = webTestClient
                .get()
                .uri(HALL_ADMIN_ENDPOINT)
                .headers(headers -> headers.setBasicAuth(user.getMail(), user.getPassword()))
                .exchange();

        //then
        responseSpec
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.halls[0].id").isEqualTo(hall.getId())
                .jsonPath("$.halls[0].seats").value(hasSize(hall.getSeats().size()));
    }

    private Hall addHall() {
        return hallRepository.save(HallFixture.createHall());
    }

    private User addUser() {
        return userRepository.save(UserFixture.createUser(UserRole.ADMIN));
    }
}
