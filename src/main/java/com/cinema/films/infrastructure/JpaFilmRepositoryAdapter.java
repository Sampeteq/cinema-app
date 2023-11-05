package com.cinema.films.infrastructure;

import com.cinema.films.application.queries.ReadFilms;
import com.cinema.films.domain.Film;
import com.cinema.films.domain.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
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
    public Optional<Film> readById(Long id) {
        return jpaFilmRepository.findById(id);
    }

    @Override
    public List<Film> readAll(ReadFilms query) {
        return jpaFilmRepository.findAll(
                titleSpec(query).and(categorySpec(query))
        );
    }

    @Override
    public boolean existsByTitle(String title) {
        return jpaFilmRepository.existsByTitle(title);
    }

    private static Specification<Film> titleSpec(ReadFilms query) {
        return (root, criteriaQuery, criteriaBuilder) -> query.title() == null ?
                criteriaBuilder.conjunction() :
                criteriaBuilder.equal(
                        root.get("title"),
                        query.title()
                );
    }

    private static Specification<Film> categorySpec(ReadFilms query) {
        return (root, criteriaQuery, criteriaBuilder) -> query.category() == null ?
                criteriaBuilder.conjunction() :
                criteriaBuilder.equal(
                        root.get("category"),
                        query.category()
                );
    }
}

interface JpaFilmRepository extends JpaRepository<Film, Long>, JpaSpecificationExecutor<Film> {
    boolean existsByTitle(String title);
}
