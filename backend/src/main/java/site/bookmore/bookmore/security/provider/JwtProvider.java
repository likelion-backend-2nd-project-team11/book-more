package site.bookmore.bookmore.security.provider;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import site.bookmore.bookmore.users.entity.Role;
import site.bookmore.bookmore.users.entity.User;

import java.util.Date;

@Component
public class JwtProvider {
    private final String SECRET;
    private final long EXPIRATION;
    private final String EMAIL_KEY = "email";
    private final String ID_KEY = "id";
    private final String ROLE_KEY = "role";

    public JwtProvider(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expiration) {
        this.SECRET = secret;
        this.EXPIRATION = expiration * 1000;
    }

    public String generateToken(User user) {
        Claims claims = Jwts.claims();
        claims.put(ID_KEY, user.getId());
        claims.put(EMAIL_KEY, user.getEmail());
        claims.put(ROLE_KEY, user.getRole().name());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.SignatureException | MalformedJwtException exception) { // 잘못된 jwt signature
        } catch (io.jsonwebtoken.ExpiredJwtException exception) { // jwt 만료
        } catch (io.jsonwebtoken.UnsupportedJwtException exception) { // 지원하지 않는 jwt
        } catch (IllegalArgumentException exception) { // 잘못된 jwt 토큰
        }

        return false;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        Long id = Long.parseLong(claims.get(ID_KEY).toString());
        String email = claims.get(EMAIL_KEY).toString();
        String roleName = claims.get(ROLE_KEY).toString();

        User user = User.builder()
                .id(id)
                .email(email)
                .role(Role.of(roleName))
                .build();

        return new UsernamePasswordAuthenticationToken(user, token, user.getAuthorities());
    }
}
