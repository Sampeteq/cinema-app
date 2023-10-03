package com.cinema.catalog.infrastructure.db;

import com.cinema.catalog.domain.Film;
import com.cinema.catalog.domain.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class SpringDataJpaFilmRepository implements FilmRepository {

    private final JpaFilmRepository jpaFilmRepository;

    @Override
    public Film add(Film film) {
        return jpaFilmRepository.save(film);
    }

    @Override
    public void delete(Film film) {
        jpaFilmRepository.delete(film);
    }

    @Override
    public Optional<Film> readByTitle(String title) {
        return jpaFilmRepository.findByTitle(title);
    }

    @Override
    public List<Film> readAll() {
        return jpaFilmRepository.findAll();
    }

    @Override
    public boolean existsByTitle(String title) {
        return jpaFilmRepository.existsByTitle(title);
    }
}

interface JpaFilmRepository extends JpaRepository<Film, Long> {
    Optional<Film> findByTitle(String title);
    boolean existsByTitle(String title);
}
