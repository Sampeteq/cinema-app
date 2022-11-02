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
@EqualsAndHashCode(of = "uuid")
@ToString
class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid = UUID.randomUUID();

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
        return new FilmDTO(this.id, this.title, this.category, this.year.getValue());
    }
}
