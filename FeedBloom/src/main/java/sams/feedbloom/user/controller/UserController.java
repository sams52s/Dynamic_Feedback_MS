package sams.feedbloom.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sams.feedbloom.authentication.service.AuthService;
import sams.feedbloom.user.dto.UserDTO;
import sams.feedbloom.user.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/web/user")
@RequiredArgsConstructor
public class UserController {
	private final AuthService authService;
	private final UserService userService;
	
	@GetMapping("/dashboard")
	public String showDashboard(Model model) {
		UserDTO userInfo = authService.getAuthenticatedUserInfo();
		model.addAttribute("user", userInfo);
		List<UserDTO> userList = userService.getAllUsers();
		model.addAttribute("userList", userList);
		return "pages/common/users";
	}
	
	@PostMapping("/update")
	public String updateUserInfo(@Valid @ModelAttribute UserDTO request) {
		UserDTO modifierUserInfo = authService.getAuthenticatedUserInfo();
		if (request == null || modifierUserInfo == null) {
			throw new IllegalArgumentException("Invalid user data for update.");
		}
		request.setUpdatedBy(modifierUserInfo.getEmail());
		userService.updateUser(request);
		return "redirect:/web/user/dashboard";
	}
	
	@GetMapping("/{id}/delete")
	public String deleteUserInfo(@PathVariable Long id) {
		UserDTO modifierUserInfo = authService.getAuthenticatedUserInfo();
		if (modifierUserInfo == null || id == null) {
			throw new IllegalArgumentException("Invalid user data for deletion.");
		}
		
		if (modifierUserInfo.getId().equals(id)) {
			throw new IllegalArgumentException("You cannot delete your own account.");
		}
		userService.deleteUser(id, modifierUserInfo.getEmail());
		return "redirect:/web/user/dashboard";
	}
}
