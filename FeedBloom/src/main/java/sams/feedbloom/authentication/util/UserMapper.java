package sams.feedbloom.authentication.util;

import sams.feedbloom.authentication.dto.RegisterRequest;
import sams.feedbloom.user.dto.UserDTO;
import sams.feedbloom.user.entity.User;

public class UserMapper {
	
	public static User mapToEntity(RegisterRequest request) {
		User user = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(request.getPassword());
		return user;
	}
	
	public static UserDTO mapToDto(User user) {
		return new UserDTO(
				user.getId(),
				user.getName(),
				user.getEmail(),
				user.getRole()
		);
	}
}