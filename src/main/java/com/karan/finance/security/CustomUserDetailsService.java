package com.karan.finance.security;

import com.karan.finance.entity.User;
import com.karan.finance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findById(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), "",
                true, true, true, true,
                AuthorityUtils.NO_AUTHORITIES);
    }
}