package site.bookmore.bookmore.oauth2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class Oauth2FailureHandler implements AuthenticationFailureHandler {
    @Value("${oauth.failure.url}")
    private String FAILURE_URL;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=UTF-8");

        PrintWriter printWriter = response.getWriter();
        printWriter.println("<script>");
        printWriter.println(String.format("alert('%s')", exception.getMessage()));
        printWriter.println(String.format("window.location.href='%s'", FAILURE_URL));
        printWriter.println("</script>");
    }
}
