package com.cinema.films.infrastrcture.db;

import com.cinema.films.domain.Film;
import com.cinema.films.domain.FilmCategory;
import com.cinema.films.domain.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

interface JpaFilmRepository extends JpaRepository<JpaFilm, Long> {

    List<JpaFilm> getByTitle(String title);

    List<JpaFilm> getByCategory(FilmCategory category);
}

@Repository
@RequiredArgsConstructor
class JpaFilmRepositoryAdapter implements FilmRepository {

    private final JpaFilmRepository jpaFilmRepository;
    private final JpaFilmMapper mapper;

    @Override
    public Film save(Film film) {
        return mapper.toDomain(jpaFilmRepository.save(mapper.toJpa(film)));
    }

    @Override
    public void delete(Film film) {
        jpaFilmRepository.delete(mapper.toJpa(film));
    }

    @Override
    public Optional<Film> getById(long id) {
        return jpaFilmRepository
                .findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Film> getAll() {
        return jpaFilmRepository
                .findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Film> getByTitle(String title) {
        return jpaFilmRepository
                .getByTitle(title)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Film> getByCategory(FilmCategory category) {
        return jpaFilmRepository
                .getByCategory(category)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

}
