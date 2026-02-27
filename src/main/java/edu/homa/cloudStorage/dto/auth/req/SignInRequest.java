package edu.homa.cloudStorage.dto.auth.req;

public record SignInRequest(
        String username,
        String password
) {
}
