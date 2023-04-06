package code.user.domain.commands;

public record SignInCommand(
        String username,
        String password
) {
}
