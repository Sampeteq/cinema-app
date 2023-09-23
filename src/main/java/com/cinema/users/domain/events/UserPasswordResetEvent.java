package com.cinema.users.domain.events;

import java.util.UUID;

public record UserPasswordResetEvent(String mail, UUID token) {
}
