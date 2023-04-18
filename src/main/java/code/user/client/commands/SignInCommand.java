package code.user.client.commands;

public record SignInCommand(
        String username,
        String password
) {
}
