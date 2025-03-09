package sams.feedbloom.authentication.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sams.feedbloom.authentication.entity.User;
import sams.feedbloom.authentication.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	private final UserRepository userRepository;
	
	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email)
		                          .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
		
		return org.springframework.security.core.userdetails.User
				       .withUsername(user.getEmail())
				       .password(user.getPassword())
				       .roles(user.getRole().name())
				       .build();
	}
}