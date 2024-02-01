package com.cinema.mails;

import lombok.Getter;

import java.time.Clock;
import java.time.LocalDateTime;

@Getter
public class MailMessage {

    private final String receiver;

    private final String subject;

    private final String text;

    private LocalDateTime sentAt;

    public MailMessage(String receiver, String subject, String text) {
        this.receiver = receiver;
        this.subject = subject;
        this.text = text;
    }

    public void sentAt(Clock clock) {
        this.sentAt = LocalDateTime.now(clock);
    }
}
