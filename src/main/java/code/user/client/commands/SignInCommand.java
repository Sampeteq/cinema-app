package code.user.client.commands;

public record SignInCommand(
        String mail,
        String password
) {
}
