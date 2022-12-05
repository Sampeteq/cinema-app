package code.films;

import code.films.dto.FilmCategoryDto;
import code.films.dto.FilmDto;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "FILMS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(of = "id")
@ToString
class Film {

    @Id
    private UUID id;

    private String title;

    @Enumerated(EnumType.STRING)
    private FilmCategory category;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "year"))
    private FilmYear year;

    FilmDto toDTO() {
        return FilmDto
                .builder()
                .id(id)
                .title(title)
                .category(FilmCategoryDto.valueOf(category.name()))
                .year(year.getValue())
                .build();
    }
}
