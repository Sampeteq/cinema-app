package com.cinema.mail;

import com.cinema.users.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
public class Mail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;

    private String content;

    private LocalDateTime sentAt;

    @ManyToOne
    private User user;

    protected Mail() {}

    public Mail(String subject, String content, LocalDateTime sentAt, User user) {
        this.subject = subject;
        this.content = content;
        this.sentAt = sentAt;
        this.user = user;
    }
}
