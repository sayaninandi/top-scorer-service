package com.intuit.topscorerservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class AccessLogFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        Instant start = Instant.now();
        try {
            chain.doFilter(request, response);
        } finally {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            log.info(getTags(request, response, time));
        }
    }

    private String getTags(final HttpServletRequest request, final HttpServletResponse response, long time) {
        final Map<String, String> tags = new HashMap<>();
        tags.put("status_code", String.valueOf(response.getStatus()));
        tags.put("status_family", response.getStatus() / 100 + "xx");
        tags.put("method", request.getMethod().toLowerCase());
        tags.put("uri", getRequestUri(request));
        tags.put("requestTime", String.valueOf(time));
        tags.put("api_version", "v1");
        return tags.toString();
    }

    static String getRequestUri(final HttpServletRequest request) {
        return request.getRequestURI()
                .toLowerCase()
                .replaceAll("scores/[a-z0-9-_]+", "score/id")
                .replaceAll("top/[0-9]+", "top/id");
    }
}
