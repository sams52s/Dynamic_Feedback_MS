package sams.feedbloom.authentication.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import sams.feedbloom.authentication.dto.AuthResponse;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
	
	private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000; // 24 hours
	private static final String JWT_COOKIE_NAME = "jwt";
	private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
			Base64.getEncoder().encodeToString("your-secure-secret-key-should-be-long".getBytes()).getBytes());
	
	public static void setJwtCookie(HttpServletResponse response, AuthResponse authResponse) {
		Cookie jwtCookie = new Cookie(JWT_COOKIE_NAME, authResponse.getToken());
		jwtCookie.setHttpOnly(true);
		jwtCookie.setPath("/web");
		jwtCookie.setMaxAge((int) (EXPIRATION_TIME / 1000)); // Convert milliseconds to seconds
		response.addCookie(jwtCookie);
	}
	
	public static void invalidateJwtCookie(HttpServletResponse response) {
		Cookie jwtCookie = new Cookie(JWT_COOKIE_NAME, null);
		jwtCookie.setHttpOnly(true);
		jwtCookie.setPath("/web");
		jwtCookie.setMaxAge(0);
		response.addCookie(jwtCookie);
	}
	
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userDetails.getUsername());
	}
	
	private String createToken(Map<String, Object> claims, String email) {
		return Jwts.builder()
		           .claims(claims)
		           .subject(email)
		           .issuedAt(new Date())
		           .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
		           .signWith(SECRET_KEY)
		           .compact();
	}
	
	public boolean validateToken(String token, UserDetails userDetails) {
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
	
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
}