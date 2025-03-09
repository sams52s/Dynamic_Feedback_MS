package sams.feedbloom.authentication.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sams.feedbloom.authentication.dto.AuthResponse;
import sams.feedbloom.authentication.dto.LoginRequest;
import sams.feedbloom.authentication.dto.RegisterRequest;
import sams.feedbloom.authentication.dto.UserDTO;
import sams.feedbloom.authentication.entity.User;
import sams.feedbloom.authentication.exception.EmailAlreadyExistsException;
import sams.feedbloom.authentication.exception.UserNotFoundException;
import sams.feedbloom.authentication.repository.UserRepository;
import sams.feedbloom.authentication.util.JwtUtil;
import sams.feedbloom.authentication.util.UserMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	
	@Transactional
	public AuthResponse registerUser(RegisterRequest request) {
		userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
			log.warn("User registration failed: Email {} already exists", request.getEmail());
			throw new EmailAlreadyExistsException("Email already exists");
		});
		
		User newUser = UserMapper.mapToEntity(request);
		newUser.setPassword(passwordEncoder.encode(request.getPassword()));
		
		User savedUser = userRepository.save(newUser);
		log.info("New user registered successfully: {}", savedUser.getEmail());
		
		return generateAuthResponse(savedUser);
	}
	
	public AuthResponse loginUser(LoginRequest request) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
		                                  );
		
		User user = userRepository.findByEmail(request.getEmail())
		                          .orElseThrow(() -> {
			                          log.warn("Login failed: Invalid email or password for {}", request.getEmail());
			                          return new UserNotFoundException("Invalid email or password");
		                          });
		
		log.info("User logged in successfully: {}", user.getEmail());
		return generateAuthResponse(user);
	}
	
	public UserDTO findUserByEmail(String email) {
		return userRepository.findByEmail(email)
		                     .map(user -> {
			                     log.info("User found: {}", user.getEmail());
			                     return UserMapper.mapToDto(user);
		                     })
		                     .orElseThrow(() -> {
			                     log.warn("User not found: {}", email);
			                     return new UserNotFoundException("User not found");
		                     });
	}
	
	private AuthResponse generateAuthResponse(User user) {
		// Convert User entity to UserDetails
		UserDetails userDetails = org.springframework.security.core.userdetails.User
				                          .withUsername(user.getEmail())
				                          .password(user.getPassword())
				                          .roles(user.getRole().name())
				                          .build();
		
		String jwtToken = jwtUtil.generateToken(userDetails); // Pass UserDetails to generateToken
		return new AuthResponse(UserMapper.mapToDto(user), jwtToken);
	}
}