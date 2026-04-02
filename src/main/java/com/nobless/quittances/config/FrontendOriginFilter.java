package com.nobless.quittances.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class FrontendOriginFilter extends OncePerRequestFilter {

    private final ApiAccessProperties apiAccessProperties;

    public FrontendOriginFilter(ApiAccessProperties apiAccessProperties) {
        this.apiAccessProperties = apiAccessProperties;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return !path.startsWith("/api/")
                || path.equals("/api/health")
                || HttpMethod.OPTIONS.matches(request.getMethod());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!apiAccessProperties.isEnforceOriginHeader()) {
            filterChain.doFilter(request, response);
            return;
        }

        String origin = request.getHeader("Origin");
        Set<String> allowedOrigins = new HashSet<>(apiAccessProperties.getAllowedOrigins());

        if (origin == null || origin.isBlank() || !allowedOrigins.contains(origin)) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"data\":null,\"state\":\"[ERROR] Access denied: invalid origin\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}