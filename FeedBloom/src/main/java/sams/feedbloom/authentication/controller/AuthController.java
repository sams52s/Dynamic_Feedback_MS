package sams.feedbloom.authentication.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sams.feedbloom.authentication.dto.AuthResponse;
import sams.feedbloom.authentication.dto.LoginRequest;
import sams.feedbloom.authentication.dto.RegisterRequest;
import sams.feedbloom.authentication.dto.UserDTO;
import sams.feedbloom.authentication.service.AuthService;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	
	@GetMapping
	public String redirectToLogin() {
		return "redirect:/auth/login";
	}
	
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("registerRequest", new RegisterRequest());
		return "auth/registration";
	}
	
	@PostMapping("/register")
	public String registerUser(@Valid @ModelAttribute RegisterRequest request, Model model) {
		AuthResponse response = authService.registerUser(request);
		model.addAttribute("user", response.getUser());
		return "redirect:/auth/login";
	}
	
	@GetMapping("/api/login")
	public String showLoginForm(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("loginRequest", new LoginRequest());
		return "auth/login";
	}
	
	@PostMapping("/api/login")
	@ResponseBody
	public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody LoginRequest request) {
		AuthResponse response = authService.loginUser(request);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/user/{email}")
	@ResponseBody
	public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
		return ResponseEntity.ok(authService.findUserByEmail(email));
	}
	
	@GetMapping("/logout")
	public String logout() {
		return "redirect:/auth/login";
	}
}
