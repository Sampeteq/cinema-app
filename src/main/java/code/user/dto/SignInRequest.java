package code.user.dto;

public record SignInRequest(
        String username,
        String password
) {
}
