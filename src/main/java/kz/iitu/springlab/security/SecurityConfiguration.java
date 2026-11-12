
package kz.iitu.springlab.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private static final int REMEMBER_ME_SECONDS = 604800;

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            PersistentTokenRepository tokenRepository,
            UserDetailsService userDetailsService
    ) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/login", "/error",
                                "/css/**", "/js/**"
                        ).permitAll()

                        .requestMatchers("/actuator/health").permitAll()

                        .requestMatchers(HttpMethod.GET, "/books/**", "/api/**")
                        .hasAnyRole("USER", "ADMIN")

                        .requestMatchers("/books/**", "/api/**", "/admin/**")
                        .hasRole("ADMIN")

                        .anyRequest().denyAll()
                )

                .formLogin(form -> form
                        .permitAll()
                )

                .rememberMe(remember -> remember
                        .tokenRepository(tokenRepository)
                        .userDetailsService(userDetailsService)
                        .tokenValiditySeconds(REMEMBER_ME_SECONDS)
                        .key("lab10-demo-remember-me-key")
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(
            DataSource dataSource
    ) {
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setDataSource(dataSource);
        return repository;
    }
}
