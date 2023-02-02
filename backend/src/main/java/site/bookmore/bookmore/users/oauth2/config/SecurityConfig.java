//package site.bookmore.bookmore.users.oauth2.config;
//
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import site.bookmore.bookmore.users.oauth2.service.CustomOAuth2UserService;
//import site.bookmore.bookmore.users.oauth2.service.TokenService;
//
//@RequiredArgsConstructor
//@Configuration
//public class SecurityConfig {
//    private final CustomOAuth2UserService oAuth2UserService;
//    private final OAuth2SuccessHandler successHandler;
//    private final TokenService tokenService;
//
//    @Bean
//    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.httpBasic().disable()
//                .csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                .antMatchers("/token/**").permitAll()
//                .anyRequest().authenticated()
//                .and()
////                .oauth2Login().loginPage("/token/expired")
//                .oauth2Login().userInfoEndpoint().userService(oAuth2UserService)
//                .and()
//                .successHandler(successHandler)
//                .userInfoEndpoint().userService(oAuth2UserService);
//
//        http.addFilterBefore(new JwtAuthFilter(tokenService), UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }
//}