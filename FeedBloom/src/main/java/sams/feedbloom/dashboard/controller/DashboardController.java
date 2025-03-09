package sams.feedbloom.dashboard.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sams.feedbloom.authentication.service.AuthService;

@Controller
@RequiredArgsConstructor
public class DashboardController {
	
	private final AuthService authService;
	
	@GetMapping("/")
	public String redirectToLogin() {
		return "redirect:/auth/api/login";
	}
	
	@GetMapping("/api/dashboard")
	public String showDashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
		if (userDetails == null) {
			throw new RuntimeException("User is not authenticated");
		}
		var user = authService.findUserByEmail(userDetails.getUsername());
		model.addAttribute("user", user);
		return "/common/dashboard";
	}
	
}
