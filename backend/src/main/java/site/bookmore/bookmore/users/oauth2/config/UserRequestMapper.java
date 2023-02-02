package site.bookmore.bookmore.users.oauth2.config;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import site.bookmore.bookmore.users.entity.User;

@Component
public class UserRequestMapper {
    public User toDto(OAuth2User oAuth2User) {
        var attributes = oAuth2User.getAttributes();
        return User.builder()
                .email((String)attributes.get("email"))
                .nickname((String)attributes.get("name"))
//                .picture((String)attributes.get("picture"))
                .build();
    }
}