package com.cinema.users.domain;

import java.util.UUID;

public record UserPasswordResetEvent(String mail, UUID token) {
}
