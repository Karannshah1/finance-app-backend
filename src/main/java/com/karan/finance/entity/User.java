package com.karan.finance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email; // from OAuth

    @Column
    private String password;
    private String name;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Ticket> tickets;
}