package com.intuit.topscorerservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base16;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Component
@Slf4j
public class MDCFilter extends OncePerRequestFilter {

    public static final String PREFIX = "RQ";
    private String responseHeader = "Response_Token";
    private String mdcTokenKey = "requestId";
    private String requestHeader = null;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            final String token;
            if (!StringUtils.isEmpty(requestHeader) && !StringUtils.isEmpty(request.getHeader(requestHeader))) {
                token = request.getHeader(requestHeader);
            } else {
                token = getRequestToken();
            }
            MDC.put(mdcTokenKey, getRequestToken());
            if (!StringUtils.isEmpty(responseHeader)) {
                response.addHeader(responseHeader, token);
            }
            chain.doFilter(request, response);
        } finally {
            MDC.remove(mdcTokenKey);
        }
    }

    private String getRequestToken() {
        return PREFIX + UUID.randomUUID().toString().replace("-", "");
    }
}
