package com.karan.finance.service;

import com.karan.finance.entity.Ticket;
import com.karan.finance.entity.User;
import com.karan.finance.repository.TicketRepository;
import com.karan.finance.repository.UserRepository;
import com.karan.finance.util.QRCodeGenerator;
import com.google.zxing.WriterException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final QRCodeGenerator qrCodeGenerator;

    public TicketService(
            TicketRepository ticketRepository,
            UserRepository userRepository,
            QRCodeGenerator qrCodeGenerator) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.qrCodeGenerator = qrCodeGenerator;
    }

    public Ticket createTicketForUser(String email) throws WriterException, IOException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        String ticketCode = UUID.randomUUID().toString();
        byte[] qrCodeImage = qrCodeGenerator.generateQRCodeImage(ticketCode, 250, 250);

        Ticket ticket = new Ticket();
        ticket.setTicketCode(ticketCode);
        ticket.setQrCodeImage(qrCodeImage);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setValid(true);
        ticket.setUser(user);

        return ticketRepository.save(ticket);
    }

    public List<Ticket> getTicketsForUser(String email) {
        return ticketRepository.findByUser_Email(email);
    }

    public boolean validateTicketCode(String code) {
        return ticketRepository.findByTicketCode(code)
                .map(Ticket::isValid)
                .orElse(false);
    }

    public List<Ticket> transferTickets(String fromEmail, String toEmail, int quantity) {
        User fromUser = userRepository.findByEmail(fromEmail)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));

        User toUser = userRepository.findByEmail(toEmail)
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        List<Ticket> senderTickets = ticketRepository.findByUser(fromUser);

        if (senderTickets.size() < quantity) {
            throw new IllegalStateException("Insufficient tickets to transfer");
        }

        List<Ticket> transferred = new ArrayList<>();

        for (int i = 0; i < quantity; i++) {
            Ticket ticket = senderTickets.get(i);
            ticket.setUser(toUser);
            transferred.add(ticketRepository.save(ticket));
        }

        return transferred;
    }
}
