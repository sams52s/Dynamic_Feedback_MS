package sams.feedbloom.authentication.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sams.feedbloom.authentication.dto.AuthResponse;
import sams.feedbloom.authentication.dto.LoginRequest;
import sams.feedbloom.authentication.dto.RegisterRequest;
import sams.feedbloom.authentication.exception.EmailAlreadyExistsException;
import sams.feedbloom.authentication.exception.UserNotFoundException;
import sams.feedbloom.authentication.util.JwtUtil;
import sams.feedbloom.authentication.util.UserMapper;
import sams.feedbloom.user.dto.UserDTO;
import sams.feedbloom.user.entity.User;
import sams.feedbloom.user.repository.UserRepository;

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
		if (userRepository.findByEmailIgnoreCase(request.getEmail()).isPresent()) {
			log.warn("User registration failed: Email {} already exists", request.getEmail());
			throw new EmailAlreadyExistsException("Email already exists");
		}
		
		User newUser = UserMapper.mapToEntity(request);
		newUser.setPassword(passwordEncoder.encode(request.getPassword()));
		User savedUser = userRepository.save(newUser);
		
		log.info("New user registered successfully: {}", savedUser.getEmail());
		return generateAuthResponse(savedUser);
	}
	
	public AuthResponse loginUser(LoginRequest request) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
		                                                                  );
		User user;
		if (authentication.isAuthenticated()) {
			user = userRepository.findByEmailIgnoreCase(request.getEmail())
			                     .orElseThrow(() -> new UserNotFoundException("Invalid email or password"));
			
			log.info("User logged in successfully: {}", user.getEmail());
		} else {
			throw new UserNotFoundException("User Authentication failed: Invalid email or password");
		}
		return generateAuthResponse(user);
	}
	
	public UserDTO findUserByEmail(String email) {
		return userRepository.findByEmailIgnoreCase(email)
		                     .map(UserMapper::mapToDto)
		                     .orElseThrow(() -> new UserNotFoundException("User not found"));
	}
	
	public UserDTO findUserById(Long id) {
		return userRepository.findById(id)
		                     .map(UserMapper::mapToDto)
		                     .orElseThrow(() -> new UserNotFoundException("User not found"));
	}
	
	private AuthResponse generateAuthResponse(User user) {
		UserDetails userDetails = org.springframework.security.core.userdetails.User
				                          .withUsername(user.getEmail())
				                          .password(user.getPassword())
				                          .roles(user.getRole().name())
				                          .build();
		
		String jwtToken = jwtUtil.generateToken(userDetails);
		return new AuthResponse(UserMapper.mapToDto(user), jwtToken);
	}
	
	public UserDTO getAuthenticatedUserInfo() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new UserNotFoundException("No authenticated user found");
		}
		
		Object principal = authentication.getPrincipal();
		if (principal instanceof UserDetails userDetails) {
			String email = userDetails.getUsername();
			return findUserByEmail(email);
		} else {
			throw new UserNotFoundException("Invalid user authentication details");
		}
	}
}
