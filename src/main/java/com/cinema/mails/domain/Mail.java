package com.cinema.mails.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "mails")
@Getter
public class Mail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String receiver;

    private String subject;

    private String text;

    private MailType type;

    private LocalDateTime sentAt;

    protected Mail() {
    }

    public Mail(String receiver, String subject, String text, MailType type) {
        this.receiver = receiver;
        this.subject = subject;
        this.text = text;
        this.type = type;
    }

    public void sentAt(LocalDateTime dateTime) {
        this.sentAt = dateTime;
    }
}
