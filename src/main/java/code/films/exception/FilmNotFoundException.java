package code.films.exception;

public class FilmNotFoundException extends FilmException {
    public FilmNotFoundException() {
        super("Film not found");
    }
}
