package com.cinema.users.application;

import com.cinema.users.application.queries.GetCurrentUserId;
import com.cinema.users.application.queries.handlers.GetCurrentUserIdHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserApi {

    private final GetCurrentUserIdHandler getCurrentUserIdHandler;

    public Long getCurrentUserId() {
        return getCurrentUserIdHandler.handle(new GetCurrentUserId());
    }
}
