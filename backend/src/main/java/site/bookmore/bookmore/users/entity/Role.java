package site.bookmore.bookmore.users.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import site.bookmore.bookmore.common.exception.bad_request.BadConstantException;

@Getter
@AllArgsConstructor
public enum Role {
    ROLE_USER, ROLE_ADMIN;

    public static Role of(String name) {
        for (Role role : values()) {
            if (role.name().contains(name)) return role;
        }
        throw new BadConstantException();
    }
}
