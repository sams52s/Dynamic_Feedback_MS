package sams.feedbloom.authentication.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import sams.feedbloom.authentication.dto.UserDTO;

@Getter
@Setter
@Service
public class AuthInfoGenerator {
	private Boolean accountNotFound = true;
	private Boolean approved = false;
	private Long userId;
	private String userName;
	private String userEmail;
	private String userRole;
	
	public void loadAuthenticatedUserInfo(UserDTO account) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication == null || !authentication.isAuthenticated()) {
			this.accountNotFound = true;
			return;
		}
		
		Object principal = authentication.getPrincipal();
		if (principal instanceof UserDetails userDetails) {
			String email = userDetails.getUsername();
			if (account != null && account.getEmail().equals(email)) {
				this.accountNotFound = false;
				this.approved = true;
				this.userId = account.getId();
				this.userName = account.getName();
				this.userEmail = account.getEmail();
				this.userRole = account.getRole().name();
			} else {
				this.accountNotFound = true;
			}
		}
	}
}
