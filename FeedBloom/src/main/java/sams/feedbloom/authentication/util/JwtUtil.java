package sams.feedbloom.authentication.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
	private static final long EXPIRATION_TIME = 86400000; // 24 hours
	private final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build(); // Secure key
	
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userDetails.getUsername());
	}
	
	private String createToken(Map<String, Object> claims, String email) {
		return Jwts.builder()
		           .claims(claims)
		           .subject(email)
		           .issuedAt(new Date(System.currentTimeMillis()))
		           .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
		           .signWith(SECRET_KEY, Jwts.SIG.HS256)
		           .compact();
	}
	
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String email = extractEmail(token);
		return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	public String extractEmail(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	private Claims extractAllClaims(String token) {
		return Jwts.parser()
		           .verifyWith(SECRET_KEY)
		           .build()
		           .parseSignedClaims(token)
		           .getPayload();
	}
	
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
}