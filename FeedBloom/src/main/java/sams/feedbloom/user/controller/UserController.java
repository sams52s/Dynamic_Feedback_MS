package sams.feedbloom.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
