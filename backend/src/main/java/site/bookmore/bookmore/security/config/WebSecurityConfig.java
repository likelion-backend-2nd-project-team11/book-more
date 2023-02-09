package site.bookmore.bookmore.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import site.bookmore.bookmore.oauth2.OAuth2SuccessHandler;
import site.bookmore.bookmore.oauth2.CustomOAuth2UserService;
import site.bookmore.bookmore.security.entrypoint.CustomAccessDeniedEntryPoint;
import site.bookmore.bookmore.security.entrypoint.CustomAuthenticationEntryPoint;
import site.bookmore.bookmore.security.fiter.JwtAuthenticationFilter;
import site.bookmore.bookmore.security.provider.JwtProvider;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler successHandler;
    private final JwtProvider jwtProvider;

    public static final String[] GET_AUTHENTICATED_REGEX_LIST = {
            "^/api/v1/users/me$",
            "^/api/v1/challenges/\\d*$",
            "^/api/v1/challenges$",
            "^/api/v1/alarms$",
            "^/api/v1/alarms/new$",
    };

    public static final String[] POST_AUTHENTICATED_REGEX_LIST = {
            "^/api/v1/users/\\d*$",
            "^/api/v1/users/me$",
            "^/api/v1/users/\\d*/follow$",
            "^/api/v1/users/verify$",
            "^/api/v1/challenges$",
            "^/api/v1/books/\\w*/reviews$",
            "^/api/v1/books/reviews/\\d*/likes$",
            "^/api/v1/alarms/\\d*/confirm$",
    };

    public static final String[] PATCH_AUTHENTICATED_REGEX_LIST = {
            "^/api/v1/challenges/\\d*$",
            "^/api/v1/books/reviews/\\d*$",
    };

    public static final String[] DELETE_AUTHENTICATED_REGEX_LIST = {
            "^/api/v1/users/\\d*$",
            "^/api/v1/users/me$",
            "^/api/v1/users/\\d*/follow$",
            "^/api/v1/challenges/\\d*$",
            "^/api/v1/books/reviews/\\d*$",
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.cors();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeHttpRequests()
                .regexMatchers(HttpMethod.GET, GET_AUTHENTICATED_REGEX_LIST).authenticated()
                .regexMatchers(HttpMethod.POST, POST_AUTHENTICATED_REGEX_LIST).authenticated()
                .regexMatchers(HttpMethod.PATCH, PATCH_AUTHENTICATED_REGEX_LIST).authenticated()
                .regexMatchers(HttpMethod.DELETE, DELETE_AUTHENTICATED_REGEX_LIST).authenticated();

        http.exceptionHandling().accessDeniedHandler(new CustomAccessDeniedEntryPoint())
                .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint());

        http.oauth2Login().userInfoEndpoint().userService(oAuth2UserService)
                .and()
                .successHandler(successHandler)
                .userInfoEndpoint().userService(oAuth2UserService);
        http.addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
