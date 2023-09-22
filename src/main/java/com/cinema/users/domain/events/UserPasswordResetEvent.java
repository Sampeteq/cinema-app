package com.cinema.users.domain.events;

import java.util.UUID;

public record UserPasswordResetEvent(
        String userMail,
        UUID userPasswordResetToken
) {
}
