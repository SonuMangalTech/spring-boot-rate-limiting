package com.sonumangal.tech.filter;

import com.sonumangal.tech.entity.UserEntity;
import com.sonumangal.tech.model.UserPrincipal;
import com.sonumangal.tech.service.RateLimitService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

/*
*   Since Spring Security has already authenticated the user, don't read the user directly from the database again.
    Get it from --> SecurityContextHolder
* */
@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimitService rateLimitService;

    public RateLimitFilter(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return FilterUtils.shouldNotFilter(uri);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !(authentication.getPrincipal() instanceof UserPrincipal userPrincipal)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        UserEntity userEntity = userPrincipal.getUserEntity();

        String apiName = getApiName(request);

        boolean isAllowed = rateLimitService.validateRateLimit(apiName, userEntity);

        if (!isAllowed) {
            response.setStatus(
                    HttpStatus.TOO_MANY_REQUESTS.value());

            response.setContentType("application/json");

            response.getWriter().write("""
                    {
                      "error": "RATE_LIMIT_EXCEEDED",
                      "message": "Too many requests"
                    }
                    """);

            return;
        }
        filterChain.doFilter(request, response);
    }

    private String getApiName(HttpServletRequest request) {
        String url = request.getRequestURI();
        return Arrays.stream(url.split("/")).skip(2).collect(Collectors.joining("/"));
    }
}
