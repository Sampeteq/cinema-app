package com.cinema.mail.infrastructure.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaMailRepository extends JpaRepository<JpaMail, UUID> {
}