package co.edu.usco.springBoot_securityMemory.service;

import java.util.Collections;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import co.edu.usco.springBoot_securityMemory.model.UserEntity;
import co.edu.usco.springBoot_securityMemory.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        String password = user.getPassword();
        // si ya trae un prefijo de encoder (ej: {bcrypt}...) no lo alteramos
        if (!password.startsWith("{")) {
            password = "{noop}" + password;
        }

        // Devuelve un UserDetails con rol prefijado correctamente
        return User.withUsername(user.getUsername())
                   .password(password)
                   .roles(user.getRole()) // User.withUsername añade ROLE_ internamente
                   .build();
    }
}

