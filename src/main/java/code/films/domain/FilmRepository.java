package code.films.domain;

import code.films.domain.queries.SearchFilmsQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FilmRepository extends JpaRepository<Film, UUID> {

    @Query("SELECT f FROM Film f WHERE :#{#params.category} is null or f.category = :#{#params.category}")
    List<Film> findBy(SearchFilmsQuery params);
}


