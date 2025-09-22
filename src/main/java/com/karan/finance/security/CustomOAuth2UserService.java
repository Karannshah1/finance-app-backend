package com.karan.finance.security;

import com.karan.finance.entity.Ticket;
import com.karan.finance.entity.User;
import com.karan.finance.repository.TicketRepository;
import com.karan.finance.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    public CustomOAuth2UserService(UserRepository userRepository, TicketRepository ticketRepository) {
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oauth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        // Save or update user
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name);
            return userRepository.save(newUser);
        });

        // Create a ticket if user has none (optional)
        if (ticketRepository.findByUser(user).isEmpty()) {
            Ticket ticket = new Ticket();
            ticket.setTicketCode(UUID.randomUUID().toString());
            ticket.setCreatedAt(LocalDateTime.now());
            ticket.setUser(user);
            ticketRepository.save(ticket);
        }

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "email"
        );
    }
}
