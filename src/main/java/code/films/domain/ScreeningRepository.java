package code.films.domain;

import code.films.domain.queries.SearchScreeningQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ScreeningRepository extends JpaRepository<Screening, UUID> {

    @Query("select distinct s from Screening s " +
            "left join fetch s.seats left join fetch s.film left join fetch s.room " +
            "where (cast(cast(:#{#params.date} as string) as timestamp) is null " +
            "or s.date = cast(cast(:#{#params.date} as string) as timestamp)) " +
            "and (:#{#params.filmId} is null or s.film.id = :#{#params.filmId})")
    List<Screening> findBy(SearchScreeningQuery params);
}
