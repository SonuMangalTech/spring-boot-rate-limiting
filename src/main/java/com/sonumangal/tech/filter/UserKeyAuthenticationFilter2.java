package com.sonumangal.tech.filter;

import com.sonumangal.tech.entity.UserEntity;
import com.sonumangal.tech.model.Constant;
import com.sonumangal.tech.model.ROLE;
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
This filter consider Guest user.
* */
@Component
@AllArgsConstructor
public class UserKeyAuthenticationFilter2 extends OncePerRequestFilter {

    private final UserRepository repository;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return FilterUtils.shouldNotFilter(uri);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        UserEntity entityResponses = null;
        String userKey = request.getHeader(Constant.X_USER_KEY);

        // TODO - if userKey is null, then assume as Guest user (userKey will be their IP)
        if (userKey == null) {
            entityResponses = UserEntity.builder()
                    .role(ROLE.GUEST).build();
        } else {
            entityResponses = repository.findByUserKey(userKey).orElse(null);

            if (entityResponses == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Invalid user-key
                return;
            }
        }

        if (entityResponses != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserPrincipal principal = new UserPrincipal(entityResponses);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    principal, null, principal.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }


        filterChain.doFilter(request, response);
    }
}
