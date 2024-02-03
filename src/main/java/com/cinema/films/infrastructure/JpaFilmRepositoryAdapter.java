package com.cinema.films.infrastructure;

import com.cinema.films.domain.Film;
import com.cinema.films.domain.FilmCategory;
import com.cinema.films.domain.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class JpaFilmRepositoryAdapter implements FilmRepository {

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
    public Optional<Film> getById(Long id) {
        return jpaFilmRepository.findById(id);
    }

    @Override
    public Optional<Film> getByTitle(String title) {
        return jpaFilmRepository.findByTitle(title);
    }

    @Override
    public List<Film> getAll() {
        return jpaFilmRepository.findAll();
    }

    @Override
    public List<Film> getByCategory(FilmCategory category) {
        return jpaFilmRepository.findByCategory(category);
    }
}

interface JpaFilmRepository extends JpaRepository<Film, Long>, JpaSpecificationExecutor<Film> {
    Optional<Film> findByTitle(String title);
    List<Film> findByCategory(FilmCategory category);
}
