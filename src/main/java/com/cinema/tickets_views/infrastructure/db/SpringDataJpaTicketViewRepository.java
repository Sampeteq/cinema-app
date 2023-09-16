package com.cinema.tickets_views.infrastructure.db;

import com.cinema.tickets_views.domain.TicketView;
import com.cinema.tickets_views.domain.ports.TicketViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class SpringDataJpaTicketViewRepository implements TicketViewRepository {

    private final JpaTicketViewRepository jpaTicketViewRepository;

    @Override
    public TicketView add(TicketView ticketView) {
        return jpaTicketViewRepository.save(ticketView);
    }

    @Override
    public Optional<TicketView> readById(Long id) {
        return jpaTicketViewRepository.findById(id);
    }

    @Override
    public List<TicketView> readAllByUserId(Long userId) {
        return jpaTicketViewRepository.readAllByUserId(userId);
    }
}

interface JpaTicketViewRepository extends JpaRepository<TicketView, Long> {
    List<TicketView> readAllByUserId(Long userId);
}
