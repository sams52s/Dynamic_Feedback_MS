package sams.feedbloom.authentication.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sams.feedbloom.authentication.service.CustomUserDetailsService;
import sams.feedbloom.authentication.util.JwtUtil;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	private final JwtUtil jwtUtil;
	private final CustomUserDetailsService customUserDetailsService;
	
	public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
		this.jwtUtil = jwtUtil;
		this.customUserDetailsService = customUserDetailsService;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String authorizationHeader = request.getHeader("Authorization");
		
		String email = null;
		String jwt = null;
		
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwt = authorizationHeader.substring(7);
			try {
				email = jwtUtil.extractEmail(jwt);
			} catch (Exception e) {
				logger.warn("Invalid JWT token");
			}
		}
		
		if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(email);
			if (jwtUtil.validateToken(jwt, userDetails)) {
				UsernamePasswordAuthenticationToken authenticationToken =
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			} else {
				logger.warn("JWT validation failed for email");
			}
		}
		
		filterChain.doFilter(request, response);
	}
	
}