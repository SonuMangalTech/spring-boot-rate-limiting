package com.sonumangal.tech.config;

import com.sonumangal.tech.exception.RateLimitException;
import com.sonumangal.tech.model.UserPrincipal;
import com.sonumangal.tech.service.RateLimitService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Aspect
@Component
@RequiredArgsConstructor
public class MethodLevelRateLimitAspect {

    private final RateLimitService rateLimitService;

    @Around("@annotation(methodLevelLimit)")
    public Object rateLimit(
            ProceedingJoinPoint joinPoint,
            MethodLevelLimit methodLevelLimit) throws Throwable {

        String apiName = methodLevelLimit.apiName();

        // Get authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        boolean allowed = rateLimitService.validateRateLimit(apiName, principal.getUserEntity());

        if (!allowed) {
            throw new RateLimitException("Rate limit exceeded");
        }

        return joinPoint.proceed();
    }
}