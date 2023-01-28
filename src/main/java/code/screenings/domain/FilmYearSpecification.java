package code.screenings.domain;

public sealed interface FilmYearSpecification permits PreviousCurrentOrNextOneFilmYearSpecification {
    boolean isSatisfyBy(int year);
}


