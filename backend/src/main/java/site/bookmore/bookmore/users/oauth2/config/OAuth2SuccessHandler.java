package site.bookmore.bookmore.users.oauth2.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import site.bookmore.bookmore.security.provider.JwtProvider;
import site.bookmore.bookmore.users.entity.User;
import site.bookmore.bookmore.users.oauth2.service.Token;
import site.bookmore.bookmore.users.oauth2.service.TokenService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtProvider jwtProvider;
    private final UserRequestMapper userRequestMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
        User user = userRequestMapper.toDto(oAuth2User);// user 바뀐다.

        String token = jwtProvider.generateToken(user); // string 으로 받는다
        log.info("{}", token);

        response.sendRedirect("https://www.bookmore.site/oauth2/redirect.html?token="+token);
    }
}
