package com.karan.finance.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.karan.finance.entity.User;

import com.karan.finance.repository.UserRepository;
import com.karan.finance.security.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // Find or create the user
        Optional<User> existingUser = userRepository.findByEmail(email);
        User user;

        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            user = new User();
            user.setEmail(email);
            user.setName(name);
            userRepository.save(user);
        }

        // Generate JWT Token
        String token = jwtUtil.generateJwtToken(email);

//        String redirectUrl = "http://localhost:3000/login?token=" + token + "&email=" + email;

//        for local
        String redirectUrl = "http://localhost:3000/dashboard";
//        String redirectUrl = "https://financelite.netlify.app/dashboard";
        response.sendRedirect(redirectUrl);

        // Return JSON response with token
//        Map<String, Object> responseBody = new HashMap<>();
//        responseBody.put("token", token);
//        responseBody.put("email", email);
//        responseBody.put("name", name);
//        responseBody.put("message", "Login successful via Google OAuth2");
//
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        response.setStatus(HttpServletResponse.SC_OK);
//        objectMapper.writeValue(response.getWriter(), responseBody);
    }
}
