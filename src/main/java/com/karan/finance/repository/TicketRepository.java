package com.karan.finance.repository;

import com.karan.finance.entity.Ticket;
import com.karan.finance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByUser(User user);

    Optional<Ticket> findByTicketCode(String ticketCode);
    List<Ticket> findByUser_Email(String email);

}
