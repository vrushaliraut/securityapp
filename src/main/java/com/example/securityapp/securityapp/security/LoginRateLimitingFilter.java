package com.example.securityapp.securityapp.security;


import com.example.securityapp.securityapp.service.RateLimiterService;
import io.github.bucket4j.Bucket;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class LoginRateLimitingFilter implements Filter {

    private final RateLimiterService rateLimiterService;

    public LoginRateLimitingFilter(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if ("/api/auth/login".equals(httpRequest.getRequestURI())
                && "POST".equalsIgnoreCase(httpRequest.getMethod())) {

            String clientIp = httpRequest.getRemoteAddr();
            Bucket bucket = rateLimiterService.resolveBucket(clientIp);

            if (!bucket.tryConsume(1)) {
                ((HttpServletResponse) response).setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("Too many login attempts. Please try again later.");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
