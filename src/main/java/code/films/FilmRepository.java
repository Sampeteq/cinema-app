package code.films;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface FilmRepository extends JpaRepository<Film, UUID> {

}
