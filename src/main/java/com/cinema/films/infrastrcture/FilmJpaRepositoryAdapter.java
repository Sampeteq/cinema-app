package com.cinema.films.infrastrcture;

import com.cinema.films.domain.Film;
import com.cinema.films.domain.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class FilmJpaRepositoryAdapter implements FilmRepository {

    private final FilmJpaRepository filmJpaRepository;

    @Override
    public Film save(Film film) {
        return filmJpaRepository.save(film);
    }

    @Override
    public void delete(Film film) {
        filmJpaRepository.delete(film);
    }

    @Override
    public Optional<Film> getById(Long id) {
        return filmJpaRepository.findById(id);
    }

    @Override
    public List<Film> getAll() {
        return filmJpaRepository.findAll();
    }

    @Override
    public List<Film> getByTitle(String title) {
        return filmJpaRepository.getByTitle(title);
    }

    @Override
    public List<Film> getByCategory(Film.Category category) {
        return filmJpaRepository.getByCategory(category);
    }
}
