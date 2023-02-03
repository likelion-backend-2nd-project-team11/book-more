package site.bookmore.bookmore.oauth2.util.mapper;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import site.bookmore.bookmore.users.entity.User;

import java.time.LocalDate;

@Component
public class UserMapper {
    public static User of(OAuth2User oAuth2User) {
        var attributes = oAuth2User.getAttributes();
        return User.builder()
                .id((Long) attributes.get("id"))
                .email((String) attributes.get("email"))
                .password("")
                .nickname((String) attributes.get("sub"))
                .birth(LocalDate.of(1900, 1, 1))
//                .picture((String)attributes.get("picture"))
                .build();
    }
}