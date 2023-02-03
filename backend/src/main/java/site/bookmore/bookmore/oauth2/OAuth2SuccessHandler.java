package site.bookmore.bookmore.oauth2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import site.bookmore.bookmore.oauth2.util.mapper.UserMapper;
import site.bookmore.bookmore.security.provider.JwtProvider;
import site.bookmore.bookmore.users.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtProvider jwtProvider;
    @Value("${oauth.redirection.url}")
    private String REDIRECTION_URL;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        User user = UserMapper.of(oAuth2User);// user 바뀐다.

        String token = jwtProvider.generateToken(user); // string 으로 받는다

        response.sendRedirect(getRedirectionURI(token));
    }

    private String getRedirectionURI(String token) {
        return UriComponentsBuilder.fromUriString(REDIRECTION_URL)
                .queryParam("token", token)
                .build()
                .toUriString();
    }
}
