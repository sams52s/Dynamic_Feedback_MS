package sams.feedbloom.authentication.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sams.feedbloom.authentication.service.CustomUserDetailsService;
import sams.feedbloom.authentication.util.JwtUtil;

import java.io.IOException;

@Slf4j  // Enables Logger
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	private final JwtUtil jwtUtil;
	private final CustomUserDetailsService customUserDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String jwt = extractJwtFromRequest(request);
		
		if (jwt != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			try {
				String email = jwtUtil.extractEmail(jwt);
				log.debug("Extracted Email: {}", email);
				
				if (email != null) {
					UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
					
					if (jwtUtil.validateToken(jwt, userDetails)) {
						UsernamePasswordAuthenticationToken authToken =
								new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
						SecurityContextHolder.getContext().setAuthentication(authToken);
						
						log.info("✅ Authentication successful for user: {}", email);
					} else {
						log.warn("❌ JWT validation failed for user: {}", email);
					}
				}
			} catch (Exception e) {
				log.error("⚠️ JWT Authentication failed: {}", e.getMessage(), e);
			}
		} else {
			log.debug("⚠️ No valid JWT found or user already authenticated.");
		}
		
		filterChain.doFilter(request, response);
	}
	
	private String extractJwtFromRequest(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			log.debug("🔍 Extracted JWT from Header");
			return token;
		}
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if ("jwt".equals(cookie.getName())) {
					log.debug("🔍 Extracted JWT from Cookie");
					return cookie.getValue();
				}
			}
		}
		
		log.debug("⚠️ No JWT found in request.");
		return null;
	}
}
