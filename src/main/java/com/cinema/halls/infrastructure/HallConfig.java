package com.cinema.halls.infrastructure;

import com.cinema.halls.domain.HallRepository;
import com.cinema.halls.domain.HallService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class HallConfig {

    @Bean
    HallService hallService(HallRepository hallRepository) {
        return new HallService(hallRepository);
    }

    @Bean
    HallRepository hallRepository(HallJpaRepository hallJpaRepository) {
        return new HallJpaRepositoryAdapter(hallJpaRepository);
    }
}
