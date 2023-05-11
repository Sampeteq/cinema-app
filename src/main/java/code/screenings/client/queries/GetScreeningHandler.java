package code.screenings.client.queries;

import code.screenings.client.dto.ScreeningDto;
import code.screenings.client.dto.ScreeningMapper;
import code.screenings.domain.Screening;
import code.screenings.domain.ScreeningReadOnlyRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.JoinType;
import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class GetScreeningHandler {

    private final ScreeningReadOnlyRepository screeningReadOnlyRepository;
    private final ScreeningMapper screeningMapper;

    @Transactional(readOnly = true)
    public List<ScreeningDto> handle(GetScreeningsQuery query) {
        var specification = dateLike(query.date())
                .and(filmLike(query.filmId()))
                .and(withFetch());
        return screeningReadOnlyRepository
                .findAll(specification)
                .stream()
                .map(screeningMapper::mapToDto)
                .toList();
    }

    private static Specification<Screening> dateLike(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> date == null ?
                criteriaBuilder.conjunction() :
                criteriaBuilder.equal(root.get("date"), date);
    }

    private static Specification<Screening> filmLike(Long filmId) {
        return (root, query, criteriaBuilder) -> filmId == null ?
                criteriaBuilder.conjunction() :
                criteriaBuilder.equal(root.get("film").get("id"), filmId);
    }

    private static Specification<Screening> withFetch() {
        return (root, query, criteriaBuilder) -> {
            root.fetch("seats", JoinType.LEFT);
            root.fetch("film", JoinType.LEFT);
            root.fetch("room", JoinType.LEFT);
            query.distinct(true);
            return criteriaBuilder.conjunction();
        };
    }
}
