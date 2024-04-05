package com.cinema.mail.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Mail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String receiver;

    private String subject;

    private String content;

    private LocalDateTime sentAt;

    protected Mail() {}

    public Mail(String receiver, String subject, String content, LocalDateTime sentAt) {
        this.receiver = receiver;
        this.subject = subject;
        this.content = content;
        this.sentAt = sentAt;
    }
}
