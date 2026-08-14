package com.sonumangal.tech.filter;

import com.sonumangal.tech.entity.UserEntity;
import com.sonumangal.tech.model.Constant;
import com.sonumangal.tech.model.UserPrincipal;
import com.sonumangal.tech.repo.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
* X-USER-KEY: NORMAL_KEY
The filter extracts it and asks Spring Security to authenticate the user.
* */
@Component
@AllArgsConstructor
public class UserKeyAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository repository;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return FilterUtils.shouldNotFilterMethod(uri);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String userKey = request.getHeader(Constant.X_USER_KEY);

        if (userKey != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            UserEntity entityResponses = repository.findByUserKey(userKey).orElse(null);

            if (entityResponses != null) {
                UserPrincipal principal = new UserPrincipal(entityResponses);

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        principal, null, principal.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
