package site.bookmore.bookmore.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import site.bookmore.bookmore.security.entrypoint.CustomAccessDeniedEntryPoint;
import site.bookmore.bookmore.security.entrypoint.CustomAuthenticationEntryPoint;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    public static final String[] GET_AUTHENTICATED_REGEX_LIST = {
    };

    public static final String[] POST_AUTHENTICATED_REGEX_LIST = {
    };

    public static final String[] PUT_AUTHENTICATED_REGEX_LIST = {
    };

    public static final String[] DELETE_AUTHENTICATED_REGEX_LIST = {
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
                .regexMatchers(HttpMethod.PUT, PUT_AUTHENTICATED_REGEX_LIST).authenticated()
                .regexMatchers(HttpMethod.DELETE, DELETE_AUTHENTICATED_REGEX_LIST).authenticated()
                .regexMatchers(ADMIN_ONLY_REGEX_LIST).hasRole("ADMIN");

        http.exceptionHandling().accessDeniedHandler(new CustomAccessDeniedEntryPoint())
                .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint());

        return http.build();
    }
}
