package code.user.application.commands;

public record UserLoginCommand(
        String mail,
        String password
) {
}
