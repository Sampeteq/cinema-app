package code.screenings.domain;

import code.screenings.domain.dto.FilmCategoryDto;
import code.screenings.domain.dto.FilmDto;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "FILMS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Getter
@ToString
public class Film {

    @Id
    private UUID id;

    private String title;

    @Enumerated(EnumType.STRING)
    private FilmCategory category;

    private int year;

    private int durationInMinutes;

    public FilmDto toDto() {
        return new FilmDto(
                this.id,
                this.title,
                FilmCategoryDto.valueOf(this.category.name()),
                year,
                durationInMinutes
        );
    }
}
