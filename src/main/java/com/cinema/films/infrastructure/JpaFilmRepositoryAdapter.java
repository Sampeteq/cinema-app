package com.cinema.films.infrastructure;

import com.cinema.films.application.dto.FilmQueryDto;
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
    public List<Film> readAll(FilmQueryDto queryDto) {
        return jpaFilmRepository.findAll(
                titleSpec(queryDto).and(categorySpec(queryDto))
        );
    }

    @Override
    public boolean existsByTitle(String title) {
        return jpaFilmRepository.existsByTitle(title);
    }

    private static Specification<Film> titleSpec(FilmQueryDto queryDto) {
        return (root, query, criteriaBuilder) -> queryDto.title() == null ?
                criteriaBuilder.conjunction() :
                criteriaBuilder.equal(
                        root.get("title"),
                        queryDto.title()
                );
    }

    private static Specification<Film> categorySpec(FilmQueryDto queryDto) {
        return (root, query, criteriaBuilder) -> queryDto.category() == null ?
                criteriaBuilder.conjunction() :
                criteriaBuilder.equal(
                        root.get("category"),
                        queryDto.category()
                );
    }
}

interface JpaFilmRepository extends JpaRepository<Film, Long>, JpaSpecificationExecutor<Film> {
    boolean existsByTitle(String title);
}
