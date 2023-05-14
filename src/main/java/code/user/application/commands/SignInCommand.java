package code.user.application.commands;

public record SignInCommand(
        String mail,
        String password
) {
}
