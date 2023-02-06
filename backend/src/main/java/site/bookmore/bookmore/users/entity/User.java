package site.bookmore.bookmore.users.entity;

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

    public static final String DEFAULT_PROFILE_IMG_PATH = "images/default-profile.png";

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

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "follow_count_id")
    private FollowCount followCount;

    public User(String email) {
        this.email = email;
    }

    private String profile = DEFAULT_PROFILE_IMG_PATH;

    @Builder
    public User(Long id, String email, String password, Role role, String nickname, Tier tier, LocalDate birth) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.tier = tier;
        this.birth = birth;
        this.role = role == null ? Role.ROLE_USER : role;
        this.followCount = FollowCount.builder()
                .followerCount(0)
                .followingCount(0)
                .build();
    }

    public void update(User user) {
        updatePassword(user.getPassword());
        updateNickname(user.getNickname());
        updateBirth(user.getBirth());
    }

    private void updatePassword(String password) {
        if (password != null) {
            this.password = password;
        }
    }

    private void updateNickname(String nickname) {
        if (nickname != null) {
            this.nickname = nickname;
        }
    }

    private void updateBirth(LocalDate birth) {
        if (birth != null) {
            this.birth = birth;
        }
    }

    public void updateProfile(String profile) {
        setProfile(profile);
    }

    private void setProfile(String profile) {
        if (profile != null) {
            this.profile = profile;
        }
    }

    public void updateProfileDefault() {
        setProfileDefault();
    }

    private void setProfileDefault() {
        this.profile = DEFAULT_PROFILE_IMG_PATH;
    }

    //권한을 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<GrantedAuthority>(List.of(new SimpleGrantedAuthority(role.name())));
    }


    // 사용자의 password 를 반환
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
