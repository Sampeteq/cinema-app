package com.cinema.films.infrastrcture;

import com.cinema.films.domain.Film;
import com.cinema.films.domain.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

interface JpaFilmRepository extends JpaRepository<Film, Long> {

    List<Film> getByTitle(String title);

    List<Film> getByCategory(Film.Category category);
}

@Repository
@RequiredArgsConstructor
class JpaFilmRepositoryAdapter implements FilmRepository {

    private final JpaFilmRepository jpaFilmRepository;

    @Override
    public Film save(Film film) {
        return jpaFilmRepository.save(film);
    }

    @Override
    public void delete(Film film) {
        jpaFilmRepository.delete(film);
    }

    @Override
    public Optional<Film> getById(long id) {
        return jpaFilmRepository.findById(id);
    }

    @Override
    public List<Film> getAll() {
        return jpaFilmRepository.findAll();
    }

    @Override
    public List<Film> getByTitle(String title) {
        return jpaFilmRepository.getByTitle(title);
    }

    @Override
    public List<Film> getByCategory(Film.Category category) {
        return jpaFilmRepository.getByCategory(category);
    }
}
