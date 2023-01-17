package site.bookmore.bookmoreserver.security.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import site.bookmore.bookmoreserver.common.dto.ErrorResponse;
import site.bookmore.bookmoreserver.common.dto.ResultResponse;
import site.bookmore.bookmoreserver.common.exception.AbstractAppException;
import site.bookmore.bookmoreserver.common.exception.unauthorized.InvalidTokenException;
import site.bookmore.bookmoreserver.common.exception.unauthorized.UserNotLoggedInException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        boolean existsToken = (boolean) request.getAttribute("existsToken");

        ObjectMapper objectMapper = new ObjectMapper();

        AbstractAppException e = existsToken ? new InvalidTokenException() : new UserNotLoggedInException();

        ResultResponse<ErrorResponse> resultResponse = ResultResponse.error(e);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(resultResponse));
    }
}
