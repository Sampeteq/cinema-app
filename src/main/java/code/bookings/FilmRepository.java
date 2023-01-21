package code.bookings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

interface FilmRepository extends JpaRepository<Film, UUID> {

    @Query(
            "SELECT f FROM Film f WHERE " +
                    ":#{#params?.category} is null or f.category = :#{#params?.category}"
    )
    List<Film> findBy(FilmSearchParams params);
}


