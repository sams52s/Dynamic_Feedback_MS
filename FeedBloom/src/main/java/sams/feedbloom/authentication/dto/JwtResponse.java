package sams.feedbloom.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
	private String accessToken;
	private String refreshToken;
	private String tokenType = "Bearer";
}
