package code.user.domain.client.commands;

public record SignInCommand(
        String username,
        String password
) {
}
