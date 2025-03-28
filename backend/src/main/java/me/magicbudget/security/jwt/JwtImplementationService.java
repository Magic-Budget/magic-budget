package me.magicbudget.security.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtImplementationService {

  private static final String key = "2D4B6150645367566B59703373367639792F423F4528482B4D6251655468576D";

  public String extractUsername(String jwtToken) {
    return extractSpecificClaim(jwtToken, Claims::getSubject);
  }

  private <T> T extractSpecificClaim(String jwtToken, Function<Claims, T> claims) {
    Claims allClaims = getAllClaims(jwtToken);
    return claims.apply(allClaims);
  }

  private Claims getAllClaims(String jwtToken) {
    try {
      return Jwts.parserBuilder().setSigningKey(getSignatureKey()).build()
          .parseClaimsJws(jwtToken).getBody();
    } catch (ExpiredJwtException e) {
      throw new RuntimeException("JWT token has expired", e);
    }
  }

  private Key getSignatureKey() {
    byte[] bytes = Base64.getDecoder().decode(key);
    return Keys.hmacShaKeyFor(bytes);
  }

  public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {

    return Jwts.builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24 * 1000))
        .signWith(getSignatureKey(), SignatureAlgorithm.HS256).compact();
  }

  public boolean validateToken(String jwtToken, UserDetails userDetails) {
    String extractUsername = extractUsername(jwtToken);
    String username = userDetails.getUsername();
    boolean check = isTokenExpired(jwtToken);
    return extractUsername.equals(username) && !check;
  }

  private boolean isTokenExpired(String jwtToken) {
    Date date = extractExpirationDate(jwtToken);
    Date when = new Date();
    return date.before(when);
  }

  private Date extractExpirationDate(String jwtToken) {
    return extractSpecificClaim(jwtToken, Claims::getExpiration);
  }
}

