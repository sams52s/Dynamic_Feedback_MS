package sams.feedbloom.feedback.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sams.feedbloom.authentication.service.AuthService;
import sams.feedbloom.feedback.dto.FeedbackResponse;
import sams.feedbloom.feedback.entity.FeedbackCategory;
import sams.feedbloom.feedback.entity.FeedbackPriority;
import sams.feedbloom.feedback.entity.FeedbackStatus;
import sams.feedbloom.feedback.service.FeedbackService;
import sams.feedbloom.project.dto.ProjectResponse;
import sams.feedbloom.project.service.ProjectService;
import sams.feedbloom.user.dto.UserDTO;

import java.util.List;

@Controller
@RequestMapping("/web/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {
	private final AuthService authService;
	private final FeedbackService feedbackService;
	private final ProjectService projectService;
	
	
	@GetMapping("/dashboard")
	public String showDashboard(Model model) {
		UserDTO userInfo = authService.getAuthenticatedUserInfo();
		List<ProjectResponse> projectResponse = projectService.getAllProjects();
		model.addAttribute("user", userInfo);
		model.addAttribute("category", FeedbackCategory.values());
		model.addAttribute("project", projectResponse);
		model.addAttribute("priority", FeedbackPriority.values());
		model.addAttribute("status", FeedbackStatus.values());
		
		return "pages/common/dashboard";
	}
	
	@GetMapping("/{id}")
	public String getById(@PathVariable Long id, Model model) {
		model.addAttribute("feedback", feedbackService.getById(id));
		return "feedback/details";
	}
	
	@GetMapping
	public String getAll(Model model) {
		List<FeedbackResponse> feedbackList = feedbackService.getAll();
		model.addAttribute("feedbackList", feedbackList);
		return "feedback/list";
	}

//	@GetMapping("/create")
//	public String showCreateForm(Model model) {
//		model.addAttribute("feedback", new FeedbackResponse());
//		return "feedback/form";
//	}
	
	@PostMapping("/create")
	public String create(@ModelAttribute FeedbackResponse feedbackResponse) {
		feedbackService.create(feedbackResponse);
		UserDTO user = authService.findUserById(feedbackResponse.getFeedbackBy());
		return "redirect:/feedbacks/dashboard/" + user.getEmail();
	}
	
	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable Long id, Model model) {
		model.addAttribute("feedback", feedbackService.getById(id));
		return "feedback/form";
	}
	
	@PostMapping("/edit/{id}")
	public String update(@PathVariable Long id, @ModelAttribute FeedbackResponse feedbackResponse) {
		feedbackService.update(id, feedbackResponse);
		return "redirect:/feedbacks";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Long id) {
		feedbackService.delete(id);
		return "redirect:/feedbacks";
	}
}
