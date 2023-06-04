package code.user.application.commands;

public record UserSignInCommand(
        String mail,
        String password
) {
}
