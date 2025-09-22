package com.karan.finance.controller;

import com.karan.finance.entity.Ticket;
import com.karan.finance.service.TicketService;
import com.google.zxing.WriterException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> purchaseTicket(@AuthenticationPrincipal UserDetails userDetails) throws WriterException, IOException {
        Ticket ticket = ticketService.createTicketForUser(userDetails.getUsername());
        return ResponseEntity.ok(Map.of(
                "ticketCode", ticket.getTicketCode(),
                "qrCodeBase64", Base64.getEncoder().encodeToString(ticket.getQrCodeImage())));
    }


    @GetMapping
    public ResponseEntity<?> getTickets(@AuthenticationPrincipal UserDetails userDetails) {
        List<Map<String, String>> tickets = ticketService.getTicketsForUser(userDetails.getUsername()).stream().map(t -> Map.of(
                "ticketCode", t.getTicketCode(),
                "qrCodeBase64", Base64.getEncoder().encodeToString(t.getQrCodeImage())
        )).collect(Collectors.toList());
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/validate/{code}")
    public ResponseEntity<?> validateTicket(@PathVariable String code) {
        boolean isValid = ticketService.validateTicketCode(code);
        return ResponseEntity.ok(Map.of("valid", isValid));
    }
}