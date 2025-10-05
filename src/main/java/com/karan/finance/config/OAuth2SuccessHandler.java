package com.karan.finance.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.karan.finance.entity.User;

import com.karan.finance.repository.UserRepository;
import com.karan.finance.security.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private static final Logger logger = LoggerFactory.getLogger(OAuth2SuccessHandler.class);

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        try{
        logger.info("OAuth2 login successful. Processing user details...");

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        if (email == null) {
            logger.error("Email attribute is null from OAuth2 provider for user: {}", name);
            getRedirectStrategy().sendRedirect(request, response, "/login?error=EmailNotFound");
            return;
        }
        // Find or create the user
        Optional<User> existingUser = userRepository.findByEmail(email);
        User user;

        if (existingUser.isPresent()) {
            user = existingUser.get();
            logger.info("Existing user found with ID: {}", user.getId());

        } else {
            logger.info("User not found. Creating a new user...");

            user = new User();
            user.setEmail(email);
            user.setName(name);
            userRepository.save(user);
            logger.info("New user created and saved with ID: {}", user.getId());

        }

        // Generate JWT Token
        logger.debug("Generating JWT token for user: {}", email);

        String token = jwtUtil.generateJwtToken(email);


//        String redirectUrl = "http://localhost:3000/login?token=" + token + "&email=" + email;

//        for local
//        String redirectUrl = "http://localhost:3000/dashboard";
        String redirectUrl = "https://financelite.netlify.app/dashboard";

        String targetUrl = UriComponentsBuilder.fromUriString(redirectUrl)
                .queryParam("token", token)
                .build().toUriString();
        logger.info("Redirecting user to target URL: {}", targetUrl);

        // 3. Clear any temporary session attributes
        clearAuthenticationAttributes(request);

        // 4. Perform the redirect
        getRedirectStrategy().sendRedirect(request, response, targetUrl);

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
    catch (Exception e) {
        // Log the full exception stack trace, which is critical for debugging
        logger.error("An unexpected error occurred in OAuth2SuccessHandler", e);
        // Redirect to a generic error page on your frontend if something goes wrong
        getRedirectStrategy().sendRedirect(request, response, "/login?error=InternalServerError");
    }
    }

}
