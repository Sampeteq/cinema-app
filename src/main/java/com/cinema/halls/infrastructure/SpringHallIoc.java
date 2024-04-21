package com.cinema.halls.infrastructure;

import com.cinema.halls.domain.HallRepository;
import com.cinema.halls.application.HallService;
import com.cinema.halls.infrastructure.db.JpaHallMapper;
import com.cinema.halls.infrastructure.db.JpaHallRepository;
import com.cinema.halls.infrastructure.db.JpaHallRepositoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SpringHallIoc {

    @Bean
    HallService hallService(HallRepository hallRepository) {
        return new HallService(hallRepository);
    }

    @Bean
    HallRepository jpaHallRepositoryAdapter(JpaHallRepository jpaHallRepository, JpaHallMapper mapper) {
        return new JpaHallRepositoryAdapter(jpaHallRepository, mapper);
    }

    @Bean
    JpaHallMapper jpaHallMapper() {
        return new JpaHallMapper();
    }
}
