package com.karan.finance.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String ticketCode;

    @Lob
    private byte[] qrCodeImage;

    private boolean valid = true;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_email")
    private User user;
}
