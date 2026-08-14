package com.sonumangal.tech.config;

import com.sonumangal.tech.filter.RateLimitFilter;
import com.sonumangal.tech.filter.UserKeyAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final UserKeyAuthenticationFilter userKeyAuthenticationFilter;
    private final RateLimitFilter rateLimitFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        return security.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/user/**").permitAll()
                        .requestMatchers("/rateLimit/getUsers").permitAll()
                        .anyRequest().authenticated()

                )
                .addFilterBefore(
                        userKeyAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilterAfter(
                        rateLimitFilter,
                        UserKeyAuthenticationFilter.class
                )
                .headers(headers ->
                        headers.frameOptions(frame ->
                                frame.sameOrigin()
                        )
                )

                .build();
    }
}
