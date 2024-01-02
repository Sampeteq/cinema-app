package com.cinema.films.infrastructure;

import com.cinema.films.application.dto.GetFilmsDto;
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
    public Optional<Film> getById(Long id) {
        return jpaFilmRepository.findById(id);
    }

    @Override
    public List<Film> getAll(GetFilmsDto dto) {
        return jpaFilmRepository.findAll(
                titleSpec(dto).and(categorySpec(dto))
        );
    }

    @Override
    public boolean existsByTitle(String title) {
        return jpaFilmRepository.existsByTitle(title);
    }

    private static Specification<Film> titleSpec(GetFilmsDto dto) {
        return (root, criteriaQuery, criteriaBuilder) -> dto.title() == null ?
                criteriaBuilder.conjunction() :
                criteriaBuilder.equal(
                        root.get("title"),
                        dto.title()
                );
    }

    private static Specification<Film> categorySpec(GetFilmsDto dto) {
        return (root, criteriaQuery, criteriaBuilder) -> dto.category() == null ?
                criteriaBuilder.conjunction() :
                criteriaBuilder.equal(
                        root.get("category"),
                        dto.category()
                );
    }
}

interface JpaFilmRepository extends JpaRepository<Film, Long>, JpaSpecificationExecutor<Film> {
    boolean existsByTitle(String title);
}
