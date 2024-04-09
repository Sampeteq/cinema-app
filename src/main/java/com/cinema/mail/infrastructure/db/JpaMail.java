package com.cinema.mail.infrastructure.db;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "mail")
@NoArgsConstructor
@AllArgsConstructor
class JpaMail {
    @Id
    private UUID id;
    private String receiver;
    private String subject;
    private String content;
    private LocalDateTime sentAt;
}
