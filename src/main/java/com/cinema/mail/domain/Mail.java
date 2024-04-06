package com.cinema.mail.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class Mail {
    private Long id;
    private String receiver;
    private String subject;
    private String content;
    private LocalDateTime sentAt;
}
