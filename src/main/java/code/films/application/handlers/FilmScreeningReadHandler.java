package code.films.application.handlers;

import code.films.application.dto.FilmScreeningDto;
import code.films.domain.FilmScreening;
import code.films.application.dto.FilmScreeningMapper;
import code.films.application.queries.FilmScreeningReadQuery;
import code.films.infrastructure.db.FilmScreeningReadOnlyRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.JoinType;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Component
@AllArgsConstructor
public class FilmScreeningReadHandler {

    private final FilmScreeningReadOnlyRepository screeningReadOnlyRepository;
    private final FilmScreeningMapper screeningMapper;

    @Transactional(readOnly = true)
    public List<FilmScreeningDto> handle(FilmScreeningReadQuery query) {
        var specification = notFinished()
                .and(dateLike(query.date()))
                .and(filmTitleLike(query.filmTitle()))
                .and(withFetch());
        return screeningReadOnlyRepository
                .findAll(specification)
                .stream()
                .sorted(Comparator.comparing(FilmScreening::getId))
                .map(screeningMapper::mapToDto)
                .toList();
    }

    private static Specification<FilmScreening> notFinished() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isFinished"), false);
    }

    private static Specification<FilmScreening> dateLike(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> date == null ?
                criteriaBuilder.conjunction() :
                criteriaBuilder.equal(root.get("date"), date);
    }

    private static Specification<FilmScreening> filmTitleLike(String filmTitle) {
        return (root, query, criteriaBuilder) -> filmTitle == null ?
                criteriaBuilder.conjunction() :
                criteriaBuilder.equal(root.get("film").get("title"), filmTitle);
    }

    private static Specification<FilmScreening> withFetch() {
        return (root, query, criteriaBuilder) -> {
            root.fetch("seats", JoinType.LEFT);
            root.fetch("film", JoinType.LEFT);
            root.fetch("room", JoinType.LEFT);
            query.distinct(true);
            return criteriaBuilder.conjunction();
        };
    }
}
