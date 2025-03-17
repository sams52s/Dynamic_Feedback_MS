package sams.feedbloom.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sams.feedbloom.authentication.util.UserMapper;
import sams.feedbloom.user.dto.UserDTO;
import sams.feedbloom.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	
	public List<UserDTO> getAllUsers() {
		return userRepository.findAll()
		                     .stream()
		                     .map(UserMapper::mapToDto)
		                     .collect(Collectors.toList());
	}
}