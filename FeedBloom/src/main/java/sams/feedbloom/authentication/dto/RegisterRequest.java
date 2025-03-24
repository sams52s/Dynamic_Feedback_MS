package sams.feedbloom.authentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
	@NotBlank(message = "Name cannot be blank")
	private String name;
	
	@NotBlank(message = "Username cannot be blank")
	private String userName;
	
	@NotBlank(message = "Email cannot be blank")
	@Email(message = "Email is not valid")
	private String email;
	
	@NotBlank(message = "Password cannot be blank")
	@Size(min = 8, message = "Password must be at least 8 characters")
	private String password;
}