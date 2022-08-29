package com.example.ticket.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface TicketRepository extends JpaRepository<Ticket, UUID> {
}
