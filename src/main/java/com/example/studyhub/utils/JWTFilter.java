package com.example.studyhub.utils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTService jwtService;

    public JWTFilter(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        if (!jwtService.isTokenValid(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token");
            return;
        }

        String email = jwtService.extractEmail(token);
        Integer userId = jwtService.extractUserId(token);

        request.setAttribute("userId", userId);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(email, null, List.of());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        if (method.equalsIgnoreCase("OPTIONS")) {
            return true;
        }

        if (path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/webjars/") ||
                path.equals("/swagger-ui.html")) {
            return true;
        }

        if (path.equals("/api/auth/login") ||
                path.equals("/api/auth/register")) {
            return true;
        }

        if (path.startsWith("/file/") ||
                path.startsWith("/uploads/") ||
                path.startsWith("/api/files/") ||
                path.startsWith("/thumbnails/")) {
            return true;
        }

        if (path.equals("/api/contact") && method.equalsIgnoreCase("POST")) {
            return true;
        }

        if (path.startsWith("/api/schools/") || path.equals("/api/schools")) {
            return true;
        }
        if (path.startsWith("/api/subjects/") || path.equals("/api/subjects")) {
            return true;
        }

        if (path.equals("/api/documents") && method.equalsIgnoreCase("GET")) {
            return true;
        }

        if (path.equals("/api/comment") && method.equalsIgnoreCase("GET")) {
            return true;
        }

        // ✅ Thêm các public endpoint khác ở đây nếu cần
        // if (path.startsWith("/api/documents/") || path.equals("/api/documents")) {
        //     return true;
        // }

        return false;
    }
}