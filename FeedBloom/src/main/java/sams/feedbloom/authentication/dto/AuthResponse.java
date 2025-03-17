package sams.feedbloom.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import sams.feedbloom.user.dto.UserDTO;

@Data
@AllArgsConstructor
public class AuthResponse {
	private UserDTO user;
	private String token;
}
