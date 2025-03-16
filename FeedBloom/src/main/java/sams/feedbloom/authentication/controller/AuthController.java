package sams.feedbloom.authentication.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import sams.feedbloom.authentication.dto.AuthResponse;
import sams.feedbloom.authentication.dto.LoginRequest;
import sams.feedbloom.authentication.dto.RegisterRequest;
import sams.feedbloom.authentication.service.AuthService;

@Controller
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	
	@GetMapping("/")
	public String redirectToLogin() {
		return "redirect:/login";
	}
	
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("request", new RegisterRequest());
		model.addAttribute("authMode", "Register");
		return "pages/auth/authentication";
	}
	
	@PostMapping("/register")
	public String registerUser(@Valid @ModelAttribute RegisterRequest request) {
		authService.registerUser(request);
		return "redirect:/login?success";
	}
	
	@GetMapping("/login")
	public String showLoginForm(Model model) {
		model.addAttribute("request", new LoginRequest());
		model.addAttribute("authMode", "Login");
		return "pages/auth/authentication";
	}
	
	@PostMapping("/login")
	public String loginUser(@Valid @ModelAttribute LoginRequest request, Model model, HttpServletResponse response) {
		AuthResponse authResponse = authService.loginUser(request);
		
		// Add the JWT token to the response (e.g., as a cookie)
		Cookie jwtCookie = new Cookie("jwt", authResponse.getToken());
		jwtCookie.setHttpOnly(true);
		jwtCookie.setPath("/feedbacks");
		response.addCookie(jwtCookie);
		jwtCookie.setMaxAge(60 * 60);
		
		// Add user details to the model
		model.addAttribute("user", authService.findUserByEmail(authResponse.getUser().getEmail()));
		
		return "redirect:/feedbacks/dashboard";
	}

//	@PostMapping("/api/login")
//	public ResponseEntity<AuthResponse> loginUserJson(@Valid @ModelAttribute LoginRequest request) {
//		return ResponseEntity.ok(authService.loginUser(request));
//	}
	
	@PostMapping("/logout")
	public String logoutUser() {
		return "redirect:/login";
	}
}
