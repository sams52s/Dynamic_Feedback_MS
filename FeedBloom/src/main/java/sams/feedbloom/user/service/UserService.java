package sams.feedbloom.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sams.feedbloom.authentication.util.UserMapper;
import sams.feedbloom.user.dto.UserDTO;
import sams.feedbloom.user.entity.User;
import sams.feedbloom.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	
	public List<UserDTO> getAllUsers() {
		return userRepository.findByIsDeletedFalse()
		                     .stream()
		                     .map(UserMapper::mapToDto)
		                     .collect(Collectors.toList());
	}
	
	public void updateUser(UserDTO request) {
		userRepository.findById(request.getId())
		              .ifPresentOrElse(user -> {
			              user.setName(request.getName());
			              user.setEmail(request.getEmail());
			              user.setRole(request.getRole());
			              user.setUpdatedAt(request.getUpdatedAt());
			              user.setUpdatedBy(request.getUpdatedBy());
			              userRepository.save(user);
		              }, () -> new RuntimeException("Project not found with ID: " + request.getId()));
	}
	
	public void deleteUser(Long id, String deletedBy) {
		userRepository.findById(id)
		              .ifPresentOrElse(user -> {
			              user.setDeletedBy(deletedBy);
			              user.setDeletedAt(LocalDateTime.now());
			              user.setIsDeleted(true);
			              userRepository.save(user);
		              }, () -> new RuntimeException("Project not found with ID: " + id));
	}
	
	public UserDTO getUserById(Long id) {
		return userRepository.findById(id)
		                     .map(UserMapper::mapToDto)
		                     .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
	}
	
	public User getUserEntityById(Long id) {
		return userRepository.findById(id)
		                     .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
	}
}