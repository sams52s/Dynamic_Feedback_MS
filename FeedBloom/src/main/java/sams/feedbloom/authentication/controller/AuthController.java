package sams.feedbloom.authentication.controller;

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
import sams.feedbloom.authentication.util.JwtUtil;

@Controller
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;

//	private static void setJwtCookie(HttpServletResponse response, AuthResponse authResponse) {
//		Cookie jwtCookie = new Cookie("jwt", authResponse.getToken());
//		jwtCookie.setHttpOnly(true);
//		jwtCookie.setPath("/web");
//		response.addCookie(jwtCookie);
//		jwtCookie.setMaxAge(60 * 60);
//	}
	
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
	public String loginUser(@Valid @ModelAttribute LoginRequest request, HttpServletResponse response) {
		AuthResponse authResponse = authService.loginUser(request);
		JwtUtil.setJwtCookie(response, authResponse);
		return "redirect:/web/feedbacks/dashboard";
	}

//	@PostMapping("/api/login")
//	public ResponseEntity<AuthResponse> loginUserJson(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
//		AuthResponse authResponse = authService.loginUser(request);
//		JwtUtil.setJwtCookie(response, authResponse.getToken());
//		return ResponseEntity.ok(authResponse);
//	}
	
	@GetMapping("/web/logout")
	public String userLogout(HttpServletResponse response) {
		JwtUtil.invalidateJwtCookie(response);
		return "redirect:/login";
	}
}
