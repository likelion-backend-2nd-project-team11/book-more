package site.bookmore.bookmore.security.fiter;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import site.bookmore.bookmore.security.provider.JwtProvider;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final String BEARER = "Bearer ";

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ServletException, IOException {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        request.setAttribute("existsToken", true); // 토큰 존재 여부 초기화

        if (isEmptyToken(token)) request.setAttribute("existsToken", false); // 토큰이 없는 경우 false로 변경

        if (token == null || !token.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        token = parseBearer(token);

        if (jwtProvider.validateToken(token)) {
            Authentication authentication = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isEmptyToken(String token) {
        return token == null || "".equals(token);
    }

    private String parseBearer(String token) {
        return token.substring(BEARER.length());
    }
}