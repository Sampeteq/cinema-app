package com.cinema.mail.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class Mail {
    private UUID id;
    private String receiver;
    private String subject;
    private String content;
    private LocalDateTime sentAt;
}
