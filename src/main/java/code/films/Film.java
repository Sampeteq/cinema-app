package code.films;

import code.films.dto.FilmDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "FILMS")
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString
class Film {

    @Id
    private UUID id = UUID.randomUUID();

    private String title;

    @Enumerated(EnumType.STRING)
    private FilmCategory category;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "year"))
    private FilmYear year;

    protected Film() {
    }

    Film(String title, FilmCategory category, FilmYear year) {
        this.title = title;
        this.category = category;
        this.year = year;
    }

    FilmDTO toDTO() {
        return new FilmDTO(this.id, this.title, this.category.name(), this.year.getValue());
    }
}
