package com.cinema.mail.infrastructure.db;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMailRepository extends JpaRepository<JpaMail, Long> {
}