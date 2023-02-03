package site.bookmore.bookmore.common.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LogFilter extends OncePerRequestFilter {
    private final String REQUEST_LABEL = String.format("%8s", "Request");
    private final String RESPONSE_LABEL = String.format("%8s", "Response");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        StopWatch stopWatch = new StopWatch();
        String remoteAddr = null != request.getHeader("X-FORWARDED-FOR") ? request.getHeader("X-FORWARDED-FOR") : request.getRemoteAddr();
        log.info("{}| {} {} {}", REQUEST_LABEL, remoteAddr, request.getMethod(), getRequestURI(request));
        stopWatch.start();
        filterChain.doFilter(request, response);
        stopWatch.stop();
        log.info("{}| {} {} {} Status:{} Time:{}ms", RESPONSE_LABEL, remoteAddr, request.getMethod(), getRequestURI(request), response.getStatus(), stopWatch.getTotalTimeMillis());
    }

    private String getRequestURI(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String params = getQueryParams(request);
        uri += params == null ? "" : "?" + params;
        return uri;
    }

    private String getQueryParams(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap.size() == 0) return null;
        return parameterMap.entrySet().stream()
                .map(element -> element.getKey() + "=" + String.join(",", element.getValue()))
                .collect(Collectors.joining("&"));
    }
}