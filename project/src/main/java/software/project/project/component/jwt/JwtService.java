package software.project.project.component.jwt;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {
    private final long EXPIRATION_TIME = 24 * 60 * 60 * 1000;

    @Value("${jwt.secret}")
    private String SECRET;

    public String generateToken(JwtMemberAccount userDetails) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("userID", userDetails.getUserID());

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public boolean validateToken(String token, JwtMemberAccount JwtMemberAccount) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }

        // catch (SignatureException e) {
        // throw new AuthException("Invalid JWT signature.");
        // }
        // catch (MalformedJwtException e) {
        // throw new AuthException("Invalid JWT token.");
        // }
        // catch (ExpiredJwtException e) {
        // throw new AuthException("Expired JWT token");
        // }
        // catch (UnsupportedJwtException e) {
        // throw new AuthException("Unsupported JWT token");
        // }
        // catch (IllegalArgumentException e) {
        // throw new AuthException("JWT token compact of handler are invalid");
        // }
    }

    private Date generateExpirationDate() {
        return (new Date(Instant.now().toEpochMilli() + EXPIRATION_TIME));
    }

    public String getUserIDFromToken(String token) throws ExpiredJwtException {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
        return (String) claims.get("userID");

    }

    public String refreshToken(JwtMemberAccount userDetails) {

        return generateToken(userDetails);
    }
}
