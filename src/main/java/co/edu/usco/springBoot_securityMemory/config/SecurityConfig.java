package co.edu.usco.springBoot_securityMemory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.core.Authentication;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class SecurityConfig {

    @Bean
    public AuthenticationSuccessHandler mySuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                Authentication authentication) throws IOException, ServletException {
                boolean isAdmin = authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                boolean isChef = authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_CHEF"));
                boolean isUser = authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_USER"));

                if (isAdmin) {
                    response.sendRedirect(request.getContextPath() + "/admin/hello");
                } else if (isChef) {
                    response.sendRedirect(request.getContextPath() + "/chef/hello");
                } else if (isUser) {
                    response.sendRedirect(request.getContextPath() + "/user/hello");
                } else {
                    response.sendRedirect(request.getContextPath() + "/login?error=true");
                }
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // recursos públicos
                .requestMatchers("/css/**", "/js/**", "/images/**", "/login", "/register").permitAll()

                // permitimos explícitamente que ADMIN y CHEF POSTeen a /admin/agregar
                .requestMatchers("/admin/agregar").hasAnyRole("ADMIN", "CHEF")

                // rutas sensibles de admin que deben seguir siendo exclusivas de ADMIN
                .requestMatchers("/admin/editar/**", "/admin/eliminar/**").hasRole("ADMIN")
                .requestMatchers("/admin/**").hasRole("ADMIN") // el resto de admin sigue solo para ADMIN

                // panel del chef (solo CHEF)
                .requestMatchers("/chef/**").hasRole("CHEF")

                // panel del usuario
                .requestMatchers("/user/**").hasRole("USER")

                // cualquier otra ruta requiere autenticación
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(mySuccessHandler())
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            );

        return http.build();
    }
}
