package site.bookmore.bookmore.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import site.bookmore.bookmore.security.entrypoint.CustomAccessDeniedEntryPoint;
import site.bookmore.bookmore.security.entrypoint.CustomAuthenticationEntryPoint;
import site.bookmore.bookmore.security.fiter.JwtAuthenticationFilter;
import site.bookmore.bookmore.security.provider.JwtProvider;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtProvider jwtProvider;

    public static final String[] GET_AUTHENTICATED_REGEX_LIST = {
            "^/api/v1/challenges/\\d{0,}$",
            "^/api/v1/challenges$",
            "^/api/v1/alarms$",
    };

    public static final String[] POST_AUTHENTICATED_REGEX_LIST = {
            "^/api/v1/users/\\d{0,}$",
            "^/api/v1/users/\\d{0,}/follow$",
            "^/api/v1/challenges$",
            "^/api/v1/books/\\w{0,}/reviews$",
            "^/api/v1/books/\\w{0,}/reviews/\\d{0,}/likes$",
    };

    public static final String[] PATCH_AUTHENTICATED_REGEX_LIST = {
            "^/api/v1/challenges/\\d{0,}$",
            "^/api/v1/books/\\w{0,}/reviews/\\d{0,}$",
    };

    public static final String[] DELETE_AUTHENTICATED_REGEX_LIST = {
            "^/api/v1/users/\\d{0,}$",
            "^/api/v1/users/\\d{0,}/follow$",
            "^/api/v1/challenges/\\d{0,}$",
            "^/api/v1/books/\\w{0,}/reviews/\\d{0,}$",
    };

    public static final String[] ADMIN_ONLY_REGEX_LIST = {
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
//                .regexMatchers(ADMIN_ONLY_REGEX_LIST).hasRole("ADMIN");

        http.exceptionHandling().accessDeniedHandler(new CustomAccessDeniedEntryPoint())
                .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint());

        http.addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
