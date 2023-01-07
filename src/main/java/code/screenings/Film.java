package code.screenings;

import code.screenings.dto.FilmCategoryDto;
import code.screenings.dto.FilmDto;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "FILMS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(of = "id")
@Getter
@ToString
class Film {

    @Id
    private UUID id;

    private String title;

    @Enumerated(EnumType.STRING)
    private FilmCategory category;

    private int year;

    private int durationInMinutes;

    FilmDto toDTO() {
        return new FilmDto(
                this.id,
                this.title,
                FilmCategoryDto.valueOf(this.category.name()),
                year,
                durationInMinutes
        );
    }
}
