package site.bookmore.bookmore.users.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import site.bookmore.bookmore.common.entity.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;


    // @Enumerated enum 이름을 DB에 저장
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.ROLE_USER;

    // @Enumerated enum 이름을 DB에 저장
    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
    private Tier tier;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false)
    private boolean enabled = true;

    @Builder
    public User(Long id, String email, String password, Role role, String nickname, Tier tier, LocalDate birth) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.tier = tier;
        this.birth = birth;
        this.role = role == null ? Role.ROLE_USER : role;
    }



    //권한을 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<GrantedAuthority>(List.of(new SimpleGrantedAuthority(role.name())));
    }


    // 사용자의 password를 반환
    @Override
    public String getPassword() {
        return this.password;
    }
    // 사용자의 id를 반환
    @Override
    public String getUsername() {
        return this.email;
    }

    // 계정 만료 여부 반환
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠금 여부 반환
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 패스워드의 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 활성화 여부 반환
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
