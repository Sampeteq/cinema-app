package code.films;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface FilmRepository extends JpaRepository<Film, Long> {

    List<Film> findByCategory(FilmCategory category);
}
